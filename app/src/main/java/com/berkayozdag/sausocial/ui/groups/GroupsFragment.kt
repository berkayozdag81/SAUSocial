package com.berkayozdag.sausocial.ui.groups

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.berkayozdag.sausocial.R
import com.berkayozdag.sausocial.data.NetworkResponse
import com.berkayozdag.sausocial.databinding.FragmentGroupsBinding
import com.berkayozdag.sausocial.model.profile.ProfileResponse
import com.berkayozdag.sausocial.ui.groups.adapters.GroupsAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GroupsFragment : Fragment() {

    private var _binding: FragmentGroupsBinding? = null
    private val binding get() = _binding!!
    private val adapter = GroupsAdapter()
    private val groupsViewModel: GroupsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGroupsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        groupsViewModel.getUsers()
        setupRecyclerview()
        onItemClick()
        setupObserves()
    }

    private fun setupRecyclerview() = with(binding) {
        val layoutManager = LinearLayoutManager(requireContext())
        val itemDecoration: RecyclerView.ItemDecoration = DividerItemDecoration(
            context,
            DividerItemDecoration.VERTICAL
        )
        recyclerViewGroups.addItemDecoration(itemDecoration)
        recyclerViewGroups.layoutManager = layoutManager
    }

    private fun setupObserves() {
        groupsViewModel.usersResponse.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResponse.Loading -> {

                }

                is NetworkResponse.Success -> {
                    loadGroups(it.data.filter { it.isGroup })
                }

                is NetworkResponse.Error -> {

                }
            }
        }
    }

    private fun loadGroups(groups: List<ProfileResponse>) = with(binding) {
        adapter.items = groups
        recyclerViewGroups.adapter = adapter
    }

    private fun onItemClick() {
        adapter.onItemClicked = { id ->
            val groupIdBundle = Bundle()
            groupIdBundle.putInt("id", id)
            findNavController().navigate(
                R.id.action_navigation_groups_to_otherProfileFragment,
                groupIdBundle
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}