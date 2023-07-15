package com.berkayozdag.sausocial.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.berkayozdag.sausocial.R
import com.berkayozdag.sausocial.common.SessionManager
import com.berkayozdag.sausocial.common.util.NetworkResponse
import com.berkayozdag.sausocial.common.util.setVisible
import com.berkayozdag.sausocial.data.entities.ProfileResponse
import com.berkayozdag.sausocial.databinding.FragmentSearchBinding
import com.berkayozdag.sausocial.ui.search.adapter.UsersAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val adapter = UsersAdapter()
    private val users = arrayListOf<ProfileResponse>() // Sadece kullanıcıların tutulduğu liste

    private val searchViewModel: SearchViewModel by viewModels()

    @Inject
    lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerview()
        onItemClick()
        initSearch()
        setupObserves()
        searchViewModel.getUsers()
    }

    private fun setupRecyclerview() = with(binding) {
        val layoutManager = LinearLayoutManager(requireContext())
        val itemDecoration: RecyclerView.ItemDecoration = DividerItemDecoration(
            context,
            DividerItemDecoration.VERTICAL
        )
        recyclerViewUserUsers.addItemDecoration(itemDecoration)
        recyclerViewUserUsers.layoutManager = layoutManager
        recyclerViewUserUsers.adapter = adapter // Adapteri burada set et
    }

    private fun setupObserves() {
        searchViewModel.usersResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResponse.Loading -> {}

                is NetworkResponse.Success -> {
                    val filteredUsers = response.data.filter { !it.isGroup }
                    users.clear()
                    users.addAll(filteredUsers)
                    setSearchedItems(binding.editTextSearch.text.toString())
                }

                is NetworkResponse.Error -> {}
            }
        }
    }

    private fun onItemClick() {
        adapter.onItemClicked = { profile ->
            if (sessionManager.getUserId() == profile.id) {
                findNavController().navigate(
                    R.id.action_navigation_search_to_navigation_profile,
                )
            } else {
                val profileIdBundle = Bundle().apply {
                    putInt("id", profile.id)
                }
                findNavController().navigate(
                    R.id.action_navigation_search_to_otherProfileFragment,
                    profileIdBundle
                )
            }
        }
    }

    private fun initSearch() = with(binding) {
        editTextSearch.addTextChangedListener {
            if (it.toString().isEmpty()) {
                recyclerViewUserUsers.setVisible(false)
                textViewNoSearch.setVisible(true)
            } else {
                recyclerViewUserUsers.setVisible(true)
                textViewNoSearch.setVisible(false)
            }
            setSearchedItems(it.toString())
        }
    }

    private fun setSearchedItems(query: String) {
        val items = users.filter { it.name.contains(query, ignoreCase = true) }
        adapter.setData(items)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}