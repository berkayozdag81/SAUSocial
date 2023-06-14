package com.berkayozdag.sausocial.ui.home.posts

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.berkayozdag.sausocial.R
import com.berkayozdag.sausocial.common.SessionManager
import com.berkayozdag.sausocial.common.setVisible
import com.berkayozdag.sausocial.model.Post
import com.berkayozdag.sausocial.ui.home.adapters.PostsAdapter
import com.facebook.shimmer.ShimmerFrameLayout
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
abstract class BasePostFragment : Fragment() {

    protected val adapter = PostsAdapter()
    protected val postViewModel by viewModels<PostViewModel>()

    @Inject
    lateinit var sessionManager: SessionManager

    abstract fun setupObserves()

    abstract fun setupListeners()

    abstract fun setupRecyclerView()

    protected fun loadPosts(posts: List<Post>) {
        adapter.setData(posts)
    }

    protected fun postItemClick() {
        adapter.postItemClicked = { id ->
            val postIdBundle = Bundle().apply {
                putInt("id", id)
            }
            findNavController().navigate(
                R.id.action_navigation_home_to_postDetailFragment,
                postIdBundle
            )
        }
    }

    protected fun userItemClicked() {
        adapter.userItemClicked = { id ->
            if (sessionManager.getUserId() == id) {
                findNavController().navigate(
                    R.id.action_navigation_home_to_navigation_profile,
                )
            } else {
                val userIdBundle = Bundle().apply {
                    putInt("id", id)
                }
                findNavController().navigate(
                    R.id.action_navigation_home_to_otherProfileFragment,
                    userIdBundle
                )
            }
        }
    }

    protected fun likeClicked() {
        adapter.likeClicked = { postId, appUserId ->
            postViewModel.postLike(appUserId, postId)
        }
        adapter.disLikeClicked = { postId, appUserId ->
            postViewModel.postDislike(appUserId, postId)
        }
    }

    protected fun startShimmerEffect(
        shimmerLayout: ShimmerFrameLayout,
        recyclerView: RecyclerView
    ) {
        shimmerLayout.startShimmer()
        shimmerLayout.setVisible(true)
        recyclerView.setVisible(false)
    }

    protected fun stopShimmerEffect(shimmerLayout: ShimmerFrameLayout, recyclerView: RecyclerView) {
        shimmerLayout.stopShimmer()
        shimmerLayout.setVisible(false)
        recyclerView.setVisible(true)
    }

}