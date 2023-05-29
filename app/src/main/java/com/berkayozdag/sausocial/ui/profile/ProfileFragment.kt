package com.berkayozdag.sausocial.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.berkayozdag.sausocial.R
import com.berkayozdag.sausocial.databinding.FragmentProfileBinding
import com.berkayozdag.sausocial.ui.home.model.PostResponseItem
import com.berkayozdag.sausocial.ui.home.adapters.PostsAdapter

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val adapter = PostsAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        setupRecyclerview()
        onItemClick()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun setupRecyclerview() = with(binding) {
        val layoutManager = LinearLayoutManager(requireContext())
        val itemDecoration: RecyclerView.ItemDecoration = DividerItemDecoration(
            context,
            DividerItemDecoration.VERTICAL
        )
        rwProfileUserPost.addItemDecoration(itemDecoration)
        rwProfileUserPost.layoutManager = layoutManager
    }

    private fun loadPosts(posts: List<PostResponseItem>) = with(binding) {
        adapter.setData(posts)
        rwProfileUserPost.adapter = adapter
    }

    private fun onItemClick() {
        adapter.onItemClicked = {
            findNavController().navigate(R.id.action_navigation_profile_to_postDetailFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}