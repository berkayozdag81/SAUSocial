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
import com.berkayozdag.sausocial.common.loadImage
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
    private var userId: Int? = null

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
        userId = arguments?.getInt("id")
        setupRecyclerview()
        postItemClicked()
        initViews()
        setupListeners()
        userId?.let { id -> profileViewModel.getUserById(id) }
        setupObservers()
    }

    private fun initViews() = with(binding.profileLayout) {
        profileLogoutBtn.setVisible(false)
        profileBackBtn.setOnClickListener {
            findNavController().popBackStack()
        }

        profileFragmentRefreshLayout.setOnRefreshListener {
            profileFragmentRefreshLayout.isRefreshing = false
            userId?.let { userId -> profileViewModel.getUserById(userId) }
        }
    }

    private fun setupListeners() = with(binding.profileLayout) {
        buttonFollow.setOnClickListener {
            userId?.let { id -> profileViewModel.follow(sessionManager.getUserId(), id) }
        }

        buttonUnFollow.setOnClickListener {
            userId?.let { id -> profileViewModel.unFollow(sessionManager.getUserId(), id) }
        }
    }

    private fun setupRecyclerview() = with(binding) {
        val layoutManager = LinearLayoutManager(requireContext())
        val itemDecoration: RecyclerView.ItemDecoration = DividerItemDecoration(
            context,
            DividerItemDecoration.VERTICAL
        )
        profileLayout.rwProfileUserPost.addItemDecoration(itemDecoration)
        profileLayout.rwProfileUserPost.layoutManager = layoutManager
        adapter.isProfile = false
    }

    private fun setupObservers() {
        profileViewModel.profileState.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResponse.Loading -> {
                    binding.profileLayout.profileProgressBar.setVisible(true)
                }

                is NetworkResponse.Success -> {
                    val profileData = response.data
                    with(binding.profileLayout) {
                        profileNameText.text = "${profileData.name} ${profileData.surname}"
                        profileFollowerCount.text = profileData.followers.size.toString()
                        profileDepartmentText.setVisible(!profileData.isGroup)
                        profileDepartmentText.text = profileData.part
                        profileFollowingCount.text = profileData.followings.size.toString()
                        profilePostCount.text = profileData.posts.size.toString()

                        val isCurrentUserFollower =
                            profileData.followers.any { it.followerId == sessionManager.getUserId() }
                        buttonUnFollow.setVisible(isCurrentUserFollower)
                        buttonFollow.setVisible(!isCurrentUserFollower)

                        profileData.profileImageUrl.let { imageUrl ->
                            profileAvatar.loadImage(imageUrl)
                        }

                        layoutNoResult.root.setVisible(response.data.posts.isEmpty())

                        profileProgressBar.setVisible(false)
                    }
                    loadPosts(profileData.posts)
                }

                is NetworkResponse.Error -> {
                    binding.profileLayout.profileProgressBar.setVisible(false)
                    context?.showToast("Kullanıcı profili getirilemedi.")
                }
            }
        }


        profileViewModel.followResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResponse.Loading -> {}

                is NetworkResponse.Success -> {
                    with(binding.profileLayout) {
                        buttonUnFollow.setVisible(true)
                        buttonFollow.setVisible(false)
                        userId?.let { id -> profileViewModel.getUserById(id) }
                    }
                }

                is NetworkResponse.Error -> {
                    context?.showToast("Kullanıcı takip edilemedi.")
                }
            }
        }

        profileViewModel.unFollowResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResponse.Loading -> {}

                is NetworkResponse.Success -> {
                    with(binding.profileLayout) {
                        buttonUnFollow.setVisible(false)
                        buttonFollow.setVisible(true)
                        userId?.let { id -> profileViewModel.getUserById(id) }
                    }
                }

                is NetworkResponse.Error -> {
                    context?.showToast("Kullanıcı takipten çıkarılamadı.")
                }
            }
        }
    }

    private fun loadPosts(posts: List<Post>) = with(binding) {
        adapter.setData(posts)
        profileLayout.rwProfileUserPost.adapter = adapter
    }

    private fun postItemClicked() {
        adapter.postItemClicked = { id ->
            val postIdBundle = Bundle().apply {
                putInt("id", id)
            }
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