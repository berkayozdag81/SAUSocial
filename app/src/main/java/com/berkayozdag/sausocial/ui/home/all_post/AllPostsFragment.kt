package com.berkayozdag.sausocial.ui.home.all_post

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.berkayozdag.sausocial.R
import com.berkayozdag.sausocial.data.NetworkResponse
import com.berkayozdag.sausocial.databinding.FragmentAllPostBinding
import com.berkayozdag.sausocial.ui.home.model.PostResponseItem
import com.berkayozdag.sausocial.ui.home.adapters.PostsAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AllPostsFragment : Fragment() {

    private var _binding: FragmentAllPostBinding? = null
    private val binding get() = _binding!!
    private val adapter = PostsAdapter()

    private val allPostViewModel by viewModels<AllPostViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAllPostBinding.inflate(inflater, container, false)
        setupRecyclerview()
        setupListeners()
        setupObserves()
        onItemClick()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun setupObserves() {
        allPostViewModel.getPosts()
        allPostViewModel.postResponse.observe(this.viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResponse.Success -> {
                    // İstek başarılı oldu, veriler kullanılabilir
                    val data = response.data
                    loadPosts(data)
                    /*Toast.makeText(this.context, "Toplam ${data.size} adet post bulundu", Toast.LENGTH_LONG)
                        .show()*/
                }
                is NetworkResponse.Error -> {
                    // İstekte bir hata oluştu
                    val errorMessage = response.errorMessage
                    Toast.makeText(this.context, errorMessage, Toast.LENGTH_LONG).show()
                }
                NetworkResponse.Loading -> {
                    // Yükleme animasyonu vb. gösterilebilir
                }
            }
        }
    }

    private fun setupListeners() = with(binding) {
        swipeRefreshLayout.setOnRefreshListener {
            swipeRefreshLayout.isRefreshing = false
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

    private fun loadPosts(posts: List<PostResponseItem>) = with(binding) {
        adapter.setData(posts)
        recyclerViewPosts.adapter = adapter
    }

    private fun onItemClick() {
        adapter.onItemClicked = { id ->
            val postIdBundle = Bundle()
            postIdBundle.putInt("id", id)
            findNavController().navigate(
                R.id.action_navigation_home_to_postDetailFragment,
                postIdBundle
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}