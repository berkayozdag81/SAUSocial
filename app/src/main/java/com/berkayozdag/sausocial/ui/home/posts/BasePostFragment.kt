package com.berkayozdag.sausocial.ui.home.posts

import android.os.Bundle
import android.view.animation.AccelerateInterpolator
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
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
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
        adapter.notifyDataSetChanged()
    }

    protected fun scrollRecyclerViewToTopOnItemReselected(recyclerView: RecyclerView) {
        val nav = activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        nav?.setOnItemReselectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    recyclerView.smoothScrollToPosition(0)
                }
            }
        }
    }

    protected fun handleScrollAnimation(recyclerView: RecyclerView) {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) {
                    animateFloatingActionButton(false)
                    animateBottomNavigationView(false)
                } else if (dy < 0) {
                    animateFloatingActionButton(true)
                    animateBottomNavigationView(true)
                }
            }
        })
    }

    private fun animateBottomNavigationView(show: Boolean) {
        val bottomNavigationView =
            activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val translationY = if (show) 0f else bottomNavigationView?.height?.toFloat() ?: 0f
        val animator = bottomNavigationView?.animate()?.translationY(translationY)?.setDuration(75)

        animator?.apply {
            interpolator = AccelerateInterpolator()
            start()
        }
    }

    private fun animateFloatingActionButton(show: Boolean) {
        val floatingActionButton =
            activity?.findViewById<FloatingActionButton>(R.id.createPostButton)
        val scaleX = if (show) 1f else 0f
        val scaleY = if (show) 1f else 0f
        val alpha = if (show) 1f else 0f
        val animator = floatingActionButton?.animate()?.scaleX(scaleX)?.scaleY(scaleY)?.alpha(alpha)
            ?.setDuration(75)

        animator?.apply {
            interpolator = AccelerateInterpolator()
            start()
        }
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