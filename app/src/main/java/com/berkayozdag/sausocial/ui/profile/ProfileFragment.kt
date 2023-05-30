package com.berkayozdag.sausocial.ui.profile

import android.content.Intent
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
import com.berkayozdag.sausocial.common.SessionManager
import com.berkayozdag.sausocial.common.showToast
import com.berkayozdag.sausocial.data.NetworkResponse
import com.berkayozdag.sausocial.databinding.FragmentProfileBinding
import com.berkayozdag.sausocial.model.Post
import com.berkayozdag.sausocial.ui.AuthenticationActivity
import com.berkayozdag.sausocial.ui.home.adapters.PostsAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val adapter = PostsAdapter()
    private val profileViewModel: ProfileViewModel by viewModels()

    @Inject
    lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (arguments?.getBoolean("isOther") == true) {
            arguments?.getInt("id")?.let { profileViewModel.getUserById(it) }
        } else {
            profileViewModel.getUserById(sessionManager.getUserId())
        }
        setupObservers()
        setupRecyclerview()
        onItemClick()
        signOut()
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

    private fun setupObservers() {
        profileViewModel.profileState.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResponse.Loading -> {
                }
                is NetworkResponse.Success -> {
                    binding.profileNameText.text = it.data.name + " " + it.data.surname
                    binding.profileFollowerCount.text = it.data.followers.size.toString()
                    binding.profileDepartmentText.text = it.data.part
                    binding.profileFollowingCount.text = it.data.followings.size.toString()
                    binding.profileFollowingCount.text = it.data.followings.size.toString()
                    binding.profilePostCount.text = it.data.posts.size.toString()
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
        rwProfileUserPost.adapter = adapter
    }

    private fun onItemClick() {
        adapter.onItemClicked = { id ->
            val postIdBundle = Bundle()
            postIdBundle.putInt("id", id)
            findNavController().navigate(
                R.id.action_navigation_profile_to_postDetailFragment,
                postIdBundle
            )
        }
    }

    private fun signOut() {
        binding.buttonFollow.setOnClickListener {
            sessionManager.setLogin(false)
            val intent = Intent(requireContext(), AuthenticationActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}