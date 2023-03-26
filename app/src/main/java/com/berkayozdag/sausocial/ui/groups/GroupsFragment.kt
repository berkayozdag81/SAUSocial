package com.berkayozdag.sausocial.ui.groups

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.berkayozdag.sausocial.common.Group
import com.berkayozdag.sausocial.common.HomeMockData
import com.berkayozdag.sausocial.databinding.FragmentGroupsBinding
import com.berkayozdag.sausocial.ui.groups.adapters.GroupsAdapter

class GroupsFragment : Fragment() {

    private var _binding: FragmentGroupsBinding? = null
    private val binding get() = _binding!!
    private val adapter = GroupsAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGroupsBinding.inflate(inflater, container, false)
        setupRecyclerview()
        onItemClick()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadGroups(HomeMockData.getGroups())
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

    private fun loadGroups(groups: List<Group>) = with(binding) {
        adapter.items = groups
        recyclerViewGroups.adapter = adapter
    }

    private fun onItemClick() {
        adapter.onItemClicked = {
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}