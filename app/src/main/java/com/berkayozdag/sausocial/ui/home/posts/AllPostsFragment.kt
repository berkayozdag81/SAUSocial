package com.berkayozdag.sausocial.ui.home.posts

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.berkayozdag.sausocial.common.util.NetworkResponse
import com.berkayozdag.sausocial.common.util.setVisible
import com.berkayozdag.sausocial.common.util.showToast
import com.berkayozdag.sausocial.databinding.FragmentAllPostBinding


class AllPostsFragment : BasePostFragment() {

    private var _binding: FragmentAllPostBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAllPostBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
        setupRecyclerView()
        scrollRecyclerViewToTopOnItemReselected(binding.recyclerViewPosts)
        handleScrollAnimation(binding.recyclerViewPosts)
        likeClicked()
        userItemClicked()
        postItemClick()
        setupObserves()
        postViewModel.getPosts()

    }

    override fun setupObserves() {
        postViewModel.allPostResponseEntity.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResponse.Success -> {
                    with(binding) {
                        stopShimmerEffect(
                            shimmerFrameLayout,
                            recyclerViewPosts
                        )
                        layoutNoResult.root.setVisible(response.data.isEmpty())
                    }
                    loadPosts(response.data)
                }

                is NetworkResponse.Error -> {
                    with(binding) {
                        stopShimmerEffect(
                            shimmerFrameLayout,
                            recyclerViewPosts
                        )
                    }
                    context?.showToast("Gönderiler yüklenirken bir hata oluştu.")
                }

                is NetworkResponse.Loading -> {
                    with(binding) {
                        startShimmerEffect(
                            shimmerFrameLayout,
                            recyclerViewPosts
                        )
                    }
                }
            }
        }

        postViewModel.postLikeResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResponse.Success -> {}

                is NetworkResponse.Error -> {
                    context?.showToast("Gönderi beğenilemedi.")
                }

                is NetworkResponse.Loading -> {}
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun setupListeners() = with(binding) {
        swipeRefreshLayout.setOnRefreshListener {
            swipeRefreshLayout.isRefreshing = false
            postViewModel.getPosts()
        }
    }

    override fun setupRecyclerView() = with(binding) {
        val layoutManager = LinearLayoutManager(requireContext())
        val itemDecoration: RecyclerView.ItemDecoration =
            DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        recyclerViewPosts.addItemDecoration(itemDecoration)
        recyclerViewPosts.layoutManager = layoutManager
        recyclerViewPosts.adapter = adapter
        adapter.appUserId = sessionManager.getUserId()
        adapter.isProfile = false
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}