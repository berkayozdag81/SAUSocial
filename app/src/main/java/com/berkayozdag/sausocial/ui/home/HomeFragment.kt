package com.berkayozdag.sausocial.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.berkayozdag.sausocial.R
import com.berkayozdag.sausocial.databinding.FragmentHomeBinding
import com.berkayozdag.sausocial.ui.home.adapters.PostsAdapter
import com.berkayozdag.sausocial.ui.home.adapters.ViewPagerAdapter
import com.berkayozdag.sausocial.ui.home.all_post.AllPostViewModel
import com.berkayozdag.sausocial.ui.home.all_post.AllPostsFragment
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val homeViewModel by viewModels<AllPostViewModel>()
    private val postsAdapter = PostsAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewPager()
        setupListeners()
        setupObserves()
    }

    private fun setupObserves() {

    }


    private fun setupListeners() = with(binding) {
        createPostButton.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_home_to_postCreateFragment)
        }
    }

    private fun setupViewPager() = with(binding) {
        val fragmentList = arrayListOf(
            AllPostsFragment(),
            PostsIFollowFragment(),
        )

        val adapter = ViewPagerAdapter(fragmentList, childFragmentManager, lifecycle)
        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = "All posts"
                }
                1 -> {
                    tab.text = "Following"
                }
            }
        }.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}