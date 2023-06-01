package com.berkayozdag.sausocial.ui.home.all_post

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.berkayozdag.sausocial.R
import com.berkayozdag.sausocial.common.SessionManager
import com.berkayozdag.sausocial.common.showToast
import com.berkayozdag.sausocial.data.NetworkResponse
import com.berkayozdag.sausocial.databinding.FragmentAllPostBinding
import com.berkayozdag.sausocial.model.Post
import com.berkayozdag.sausocial.ui.home.adapters.PostsAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AllPostsFragment : Fragment() {

    private var _binding: FragmentAllPostBinding? = null
    private val binding get() = _binding!!
    private val adapter = PostsAdapter()
    private val allPostViewModel by viewModels<AllPostViewModel>()

    @Inject
    lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAllPostBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerview()
        setupListeners()
        postItemClick()
        userItemClicked()
        allPostViewModel.getPosts()
        setupObserves()
    }

    private fun setupObserves() {
        allPostViewModel.postResponse.observe(this.viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResponse.Success -> {
                    loadPosts(response.data)
                }
                is NetworkResponse.Error -> {
                    context?.showToast(response.errorMessage)
                }
                NetworkResponse.Loading -> {
                }
            }
        }
    }

    private fun setupListeners() = with(binding) {
        swipeRefreshLayout.setOnRefreshListener {
            swipeRefreshLayout.isRefreshing = false
            allPostViewModel.getPosts()
        }
    }

    private fun setupRecyclerview() = with(binding) {
        val layoutManager = LinearLayoutManager(requireContext())
        val itemDecoration: RecyclerView.ItemDecoration = DividerItemDecoration(
            context,
            DividerItemDecoration.VERTICAL
        )
        recyclerViewPosts.addItemDecoration(itemDecoration)
        recyclerViewPosts.layoutManager = layoutManager
    }

    private fun loadPosts(posts: List<Post>) = with(binding) {
        adapter.setData(posts)
        recyclerViewPosts.adapter = adapter
    }

    private fun postItemClick() {
        adapter.postItemClicked = { id ->
            val postIdBundle = Bundle()
            postIdBundle.putInt("id", id)
            findNavController().navigate(
                R.id.action_navigation_home_to_postDetailFragment,
                postIdBundle
            )
        }
    }


    private fun userItemClicked() {
        adapter.userItemClicked = { id ->
            if (sessionManager.getUserId() == id) {
                findNavController().navigate(
                    R.id.action_navigation_home_to_navigation_profile,
                )
            } else {
                val userIdBundle = Bundle()
                userIdBundle.putInt("id", id)
                findNavController().navigate(
                    R.id.action_navigation_home_to_otherProfileFragment,
                    userIdBundle
                )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}