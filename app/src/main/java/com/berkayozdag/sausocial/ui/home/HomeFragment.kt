package com.berkayozdag.sausocial.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.berkayozdag.sausocial.R
import com.berkayozdag.sausocial.common.SessionManager
import com.berkayozdag.sausocial.common.util.loadImage
import com.berkayozdag.sausocial.databinding.FragmentHomeBinding
import com.berkayozdag.sausocial.ui.home.adapters.ViewPagerAdapter
import com.berkayozdag.sausocial.ui.home.posts.AllPostsFragment
import com.berkayozdag.sausocial.ui.home.posts.PostsIFollowFragment
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewPager()
        setupListeners()
        initViews()
    }

    private fun initViews() {
        binding.profileAvatar.loadImage(sessionManager.getUserProfileImage())
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
                    tab.text = "Tüm gönderiler"
                }
                1 -> {
                    tab.text = "Takip edilenler"
                }
            }
        }.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}