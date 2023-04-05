package com.berkayozdag.sausocial.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.berkayozdag.sausocial.R
import com.berkayozdag.sausocial.common.HomeMockData
import com.berkayozdag.sausocial.common.Post
import com.berkayozdag.sausocial.common.User
import com.berkayozdag.sausocial.databinding.FragmentSearchBinding
import com.berkayozdag.sausocial.ui.groups.GroupsFragment
import com.berkayozdag.sausocial.ui.home.AllPostsFragment
import com.berkayozdag.sausocial.ui.home.PostsIFollowFragment
import com.berkayozdag.sausocial.ui.home.adapters.PostsAdapter
import com.berkayozdag.sausocial.ui.home.adapters.ViewPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val adapter = UsersAdapter()
    private val userItems = arrayListOf<User>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val searchViewModel =
            ViewModelProvider(this).get(SearchViewModel::class.java)

        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setupRecyclerview()
        onItemClick()
        initSearch()
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadUser(HomeMockData.getUser())
    }

    private fun setupRecyclerview() = with(binding) {
        val layoutManager = LinearLayoutManager(requireContext())
        val itemDecoration: RecyclerView.ItemDecoration = DividerItemDecoration(
            context,
            DividerItemDecoration.VERTICAL
        )
        searchFragmentRecyclerView.addItemDecoration(itemDecoration)
        searchFragmentRecyclerView.layoutManager = layoutManager
    }

    private fun loadUser(users: List<User>) = with(binding) {
        adapter.setData(users)
        searchFragmentRecyclerView.adapter = adapter

        for (item in users.iterator()) {
            userItems.add(item)
        }
    }

    private fun onItemClick() {
        adapter.onItemClicked = {
            //findNavController().navigate(R.id.action_navigation_search_to_navigation_profile)
            Toast.makeText(context, "Kullanıcı profili", Toast.LENGTH_SHORT).show()
        }
    }

    private fun initSearch() = with(binding) {
        editTextSearch.addTextChangedListener {
            if (it.toString().isEmpty()) {
                searchFragmentRecyclerView.visibility = View.GONE
                searchFragmentNonSearchText.visibility = View.VISIBLE
                val searchQuery = it.toString()
                setSearchedItems(searchQuery)
            } else {
                searchFragmentRecyclerView.visibility = View.VISIBLE
                searchFragmentNonSearchText.visibility = View.GONE
                val searchQuery = it.toString()
                setSearchedItems(searchQuery)
            }
        }
    }

    private fun setSearchedItems(query: String) {
        val items = userItems.filter { it.userName.contains(query, ignoreCase = true) }
        adapter.setData(items)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}