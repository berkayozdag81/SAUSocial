package com.berkayozdag.sausocial.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.berkayozdag.sausocial.R
import com.berkayozdag.sausocial.databinding.FragmentHomeBinding
import com.berkayozdag.sausocial.ui.home.adapters.ViewPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        setupViewPager()
        setupListeners()
        return binding.root
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
        _binding = null
    }
}