package com.berkayozdag.sausocial.ui.home.posts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.berkayozdag.sausocial.common.setVisible
import com.berkayozdag.sausocial.common.showToast
import com.berkayozdag.sausocial.data.NetworkResponse
import com.berkayozdag.sausocial.databinding.FragmentPostsIFollowBinding


class PostsIFollowFragment : BasePostFragment() {

    private var _binding: FragmentPostsIFollowBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPostsIFollowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
        setupRecyclerView()
        likeClicked()
        userItemClicked()
        postItemClick()
        setupObserves()
        postViewModel.getFollowingPosts(sessionManager.getUserId())
    }

    override fun setupObserves() {
        postViewModel.followingPosts.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResponse.Success -> {
                    stopShimmerEffect(
                        binding.shimmerFrameLayout,
                        binding.recyclerViewPosts
                    )
                    binding.layoutNoResult.root.setVisible(response.data.isEmpty())
                    loadPosts(response.data)
                }
                is NetworkResponse.Error -> {
                    stopShimmerEffect(
                        binding.shimmerFrameLayout,
                        binding.recyclerViewPosts
                    )
                    context?.showToast(response.errorMessage)
                }
                NetworkResponse.Loading -> {
                    startShimmerEffect(
                        binding.shimmerFrameLayout,
                        binding.recyclerViewPosts
                    )
                }
            }
        }

        postViewModel.postLikeResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResponse.Success -> {
                    // İşleme devam et
                }
                is NetworkResponse.Error -> {
                    context?.showToast(response.errorMessage)
                }
                NetworkResponse.Loading -> {
                    // İşleme devam et
                }
            }
        }
    }

    override fun setupListeners() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            binding.swipeRefreshLayout.isRefreshing = false
            postViewModel.getPosts()
        }
    }

    override fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(requireContext())
        val itemDecoration: RecyclerView.ItemDecoration =
            DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        binding.recyclerViewPosts.addItemDecoration(itemDecoration)
        binding.recyclerViewPosts.layoutManager = layoutManager
        binding.recyclerViewPosts.adapter = adapter
        adapter.appUserId = sessionManager.getUserId()
        adapter.isProfile = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}