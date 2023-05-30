package com.berkayozdag.sausocial.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.berkayozdag.sausocial.R
import com.berkayozdag.sausocial.common.HomeMockData
import com.berkayozdag.sausocial.common.User
import com.berkayozdag.sausocial.data.NetworkResponse
import com.berkayozdag.sausocial.databinding.FragmentSearchBinding
import com.berkayozdag.sausocial.model.profile.ProfileResponse
import com.berkayozdag.sausocial.ui.profile.ProfileViewModel
import com.berkayozdag.sausocial.ui.search.adapter.UsersAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null

    private val binding get() = _binding!!

    private val adapter = UsersAdapter()
    private val userItems = arrayListOf<ProfileResponse>()
    private val searchViewModel: SearchViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setupObserves()
        onItemClick()
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchViewModel.getUsers()
        setupRecyclerview()
        initSearch()
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

    private fun setupObserves() {
        searchViewModel.usersResponse.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResponse.Loading -> {

                }
                is NetworkResponse.Success -> {
                    loadUser(it.data)
                }
                is NetworkResponse.Error -> {

                }
            }
        }
    }

    private fun loadUser(users: List<ProfileResponse>) = with(binding) {
        adapter.setData(users)
        searchFragmentRecyclerView.adapter = adapter

        for (item in users.iterator()) {
            userItems.add(item)
        }
    }

    private fun onItemClick() {
        adapter.onItemClicked = {
            val profileIdBundle = Bundle()
            profileIdBundle.putInt("id", it.id)
            profileIdBundle.putBoolean("isOther",true)
            findNavController().navigate(
                R.id.action_navigation_search_to_navigation_profile,
                profileIdBundle
            )
            //Toast.makeText(context, "Kullanıcı profili", Toast.LENGTH_SHORT).show()
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
        val items = userItems.filter { it.name.contains(query, ignoreCase = true) }
        adapter.setData(items)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}