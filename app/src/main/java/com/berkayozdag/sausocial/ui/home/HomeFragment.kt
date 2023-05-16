package com.berkayozdag.sausocial.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.berkayozdag.sausocial.R
import com.berkayozdag.sausocial.data.NetworkResponse
import com.berkayozdag.sausocial.databinding.FragmentHomeBinding
import com.berkayozdag.sausocial.ui.authentication.LoginViewModel
import com.berkayozdag.sausocial.ui.home.adapters.ViewPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val homeViewModel by viewModels<HomeViewModel>()

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
        homeViewModel.getPosts()
        homeViewModel.postResponse.observe(this.viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResponse.Success -> {
                    // İstek başarılı oldu, veriler kullanılabilir
                    val data = response.data
                    Toast.makeText(this.context, "Toplam ${data.size} adet post bulundu", Toast.LENGTH_LONG)
                        .show()
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