package com.berkayozdag.sausocial.ui.other_profile

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
import com.berkayozdag.sausocial.common.setVisible
import com.berkayozdag.sausocial.common.showToast
import com.berkayozdag.sausocial.data.NetworkResponse
import com.berkayozdag.sausocial.databinding.FragmentOtherProfileBinding
import com.berkayozdag.sausocial.model.Post
import com.berkayozdag.sausocial.ui.home.adapters.PostsAdapter
import com.berkayozdag.sausocial.ui.profile.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OtherProfileFragment : Fragment() {

    private var _binding: FragmentOtherProfileBinding? = null
    private val binding get() = _binding!!
    private val adapter = PostsAdapter()
    private val profileViewModel: ProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOtherProfileBinding.inflate(inflater, container, false)
        initViews()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.getInt("id")?.let { profileViewModel.getUserById(it) }
        setupObservers()
        setupRecyclerview()
        onItemClick()
    }

    private fun initViews() {
        binding.profileLayout.profileLogoutBtn.setVisible(false)
        binding.profileLayout.profileBackBtn.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setupRecyclerview() = with(binding) {
        val layoutManager = LinearLayoutManager(requireContext())
        val itemDecoration: RecyclerView.ItemDecoration = DividerItemDecoration(
            context,
            DividerItemDecoration.VERTICAL
        )
        binding.profileLayout.rwProfileUserPost.addItemDecoration(itemDecoration)
        binding.profileLayout.rwProfileUserPost.layoutManager = layoutManager
    }

    private fun setupObservers() {
        profileViewModel.profileState.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResponse.Loading -> {
                }
                is NetworkResponse.Success -> {
                    binding.profileLayout.profileNameText.text =
                        it.data.name + " " + it.data.surname
                    binding.profileLayout.profileFollowerCount.text =
                        it.data.followers.size.toString()
                    binding.profileLayout.profileDepartmentText.text = it.data.part
                    binding.profileLayout.profileFollowingCount.text =
                        it.data.followings.size.toString()
                    binding.profileLayout.profileFollowingCount.text =
                        it.data.followings.size.toString()
                    binding.profileLayout.profilePostCount.text = it.data.posts.size.toString()
                    loadPosts(it.data.posts)
                }
                is NetworkResponse.Error -> {
                    context?.showToast("Hatalı kullanıcı adı veya şifre")
                }
            }
        }
    }

    private fun loadPosts(posts: List<Post>) = with(binding) {
        adapter.setData(posts)
        binding.profileLayout.rwProfileUserPost.adapter = adapter
    }

    private fun onItemClick() {
        adapter.onItemClicked = { id ->
            val postIdBundle = Bundle()
            postIdBundle.putInt("id", id)
            findNavController().navigate(
                R.id.action_otherProfileFragment_to_postDetailFragment,
                postIdBundle
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}