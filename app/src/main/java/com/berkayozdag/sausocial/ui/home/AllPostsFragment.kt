package com.berkayozdag.sausocial.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.berkayozdag.sausocial.common.HomeMockData
import com.berkayozdag.sausocial.common.Post
import com.berkayozdag.sausocial.databinding.FragmentAllPostBinding
import com.berkayozdag.sausocial.ui.home.adapters.PostsAdapter

class AllPostsFragment : Fragment() {

    private var _binding: FragmentAllPostBinding? = null
    private val binding get() = _binding!!
    private val adapter = PostsAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAllPostBinding.inflate(inflater, container, false)
        setupRecyclerview()
        initViews()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadTransactions(HomeMockData.getPost())
    }

    private fun initViews() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            binding.swipeRefreshLayout.isRefreshing = false
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

    private fun loadTransactions(posts: List<Post>) = with(binding) {
        adapter.setData(posts)
        recyclerViewPosts.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}