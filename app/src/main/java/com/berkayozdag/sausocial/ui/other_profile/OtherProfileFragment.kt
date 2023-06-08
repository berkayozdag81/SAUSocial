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
import com.berkayozdag.sausocial.common.SessionManager
import com.berkayozdag.sausocial.common.setVisible
import com.berkayozdag.sausocial.common.showToast
import com.berkayozdag.sausocial.data.NetworkResponse
import com.berkayozdag.sausocial.databinding.FragmentOtherProfileBinding
import com.berkayozdag.sausocial.model.Post
import com.berkayozdag.sausocial.ui.home.adapters.PostsAdapter
import com.berkayozdag.sausocial.ui.profile.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class OtherProfileFragment : Fragment() {

    private var _binding: FragmentOtherProfileBinding? = null
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
        _binding = FragmentOtherProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerview()
        postItemClicked()
        initViews()
        setListener()
        arguments?.getInt("id")?.let { userId -> profileViewModel.getUserById(userId) }
        setupObservers()
    }

    private fun initViews() {
        binding.profileLayout.profileLogoutBtn.setVisible(false)
        binding.profileLayout.profileBackBtn.setOnClickListener {
            findNavController().popBackStack()
        }


    }

    private fun setListener() {
        binding.profileLayout.buttonFollow.setOnClickListener {
            arguments?.getInt("id")
                ?.let { it1 -> profileViewModel.follow(sessionManager.getUserId(), it1) }
        }

        binding.profileLayout.buttonUnFollow.setOnClickListener {
            arguments?.getInt("id")
                ?.let { id -> profileViewModel.unFollow(sessionManager.getUserId(), id) }
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
        profileViewModel.profileState.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResponse.Loading -> {
                }

                is NetworkResponse.Success -> {
                    binding.profileLayout.profileNameText.text =
                        response.data.name + " " + response.data.surname
                    binding.profileLayout.profileFollowerCount.text =
                        response.data.followers.size.toString()
                    binding.profileLayout.profileDepartmentText.text = response.data.part
                    binding.profileLayout.profileFollowingCount.text =
                        response.data.followings.size.toString()
                    binding.profileLayout.profilePostCount.text =
                        response.data.posts.size.toString()

                    if (response.data.followers.any { it.followerId == sessionManager.getUserId() }) {
                        binding.profileLayout.buttonUnFollow.setVisible(true)
                        binding.profileLayout.buttonFollow.setVisible(false)
                    }
                    loadPosts(response.data.posts)
                }

                is NetworkResponse.Error -> {
                    context?.showToast("Hatalı kullanıcı adı veya şifre")
                }
            }
        }

        profileViewModel.followResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResponse.Loading -> {
                }

                is NetworkResponse.Success -> {
                    binding.profileLayout.buttonUnFollow.setVisible(true)
                    binding.profileLayout.buttonFollow.setVisible(false)
                    arguments?.getInt("id")?.let { userId -> profileViewModel.getUserById(userId) }
                }

                is NetworkResponse.Error -> {
                    context?.showToast("İstek başarısız")
                }
            }
        }

        profileViewModel.unFollowResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResponse.Loading -> {
                }

                is NetworkResponse.Success -> {
                    binding.profileLayout.buttonUnFollow.setVisible(false)
                    binding.profileLayout.buttonFollow.setVisible(true)
                    arguments?.getInt("id")?.let { userId -> profileViewModel.getUserById(userId) }
                }

                is NetworkResponse.Error -> {
                    context?.showToast("İstek başarısız")
                }
            }
        }
    }

    private fun loadPosts(posts: List<Post>) = with(binding) {
        adapter.setData(posts)
        binding.profileLayout.rwProfileUserPost.adapter = adapter
    }

    private fun postItemClicked() {
        adapter.postItemClicked = { id ->
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