package com.berkayozdag.sausocial.ui.profile

import android.app.AlertDialog
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
import com.berkayozdag.sausocial.common.loadImage
import com.berkayozdag.sausocial.common.setVisible
import com.berkayozdag.sausocial.common.showToast
import com.berkayozdag.sausocial.data.NetworkResponse
import com.berkayozdag.sausocial.databinding.FragmentProfileBinding
import com.berkayozdag.sausocial.databinding.PostDeleteDialogBinding
import com.berkayozdag.sausocial.databinding.SignOutDialogBinding
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
        profileViewModel.getUserById(sessionManager.getUserId())
        initViews()
        likeClicked()
        setupListeners()
        setupObservers()
        setupRecyclerview()
        postItemClicked()
        postDelete()
    }

    private fun initViews() = with(binding) {
        buttonBack.setVisible(false)
        buttonFollow.setVisible(false)
        buttonUnFollow.setVisible(false)
    }

    private fun setupListeners() = with(binding) {
        buttonLogout.setOnClickListener {
            signOut()
        }

        profileFragmentRefreshLayout.setOnRefreshListener {
            profileFragmentRefreshLayout.isRefreshing = false
            profileViewModel.getUserById(sessionManager.getUserId())
        }
    }


    private fun setupRecyclerview() = with(binding) {
        val layoutManager = LinearLayoutManager(requireContext())
        val itemDecoration: RecyclerView.ItemDecoration = DividerItemDecoration(
            context,
            DividerItemDecoration.VERTICAL
        )
        recyclerViewUserPosts.addItemDecoration(itemDecoration)
        recyclerViewUserPosts.layoutManager = layoutManager
        adapter.appUserId = sessionManager.getUserId()
        adapter.isProfile = true
    }

    private fun setupObservers() {
        profileViewModel.profileState.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResponse.Loading -> {
                    binding.profileProgressBar.setVisible(true)
                }

                is NetworkResponse.Success -> {
                    with(binding) {
                        response.data.profileImageUrl.let { url ->
                            imageViewAvatar.loadImage(url)
                        }
                        textViewName.text = "${response.data.name} ${response.data.surname}"
                        textViewFollowerCount.text = response.data.followers.size.toString()
                        textViewDepartment.text = response.data.part
                        textViewFollowingCount.text =
                            response.data.followings.size.toString()
                        textViewPostCount.text = response.data.posts.size.toString()
                        layoutNoResult.root.setVisible(response.data.posts.isEmpty())
                        profileProgressBar.setVisible(false)
                    }
                    loadPosts(response.data.posts)
                }

                is NetworkResponse.Error -> {
                    binding.profileProgressBar.setVisible(false)
                    context?.showToast("Kullanıcı profili getirilemedi.")
                }
            }
        }

        profileViewModel.postDeleteResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResponse.Loading -> {}

                is NetworkResponse.Success -> {
                    profileViewModel.getUserById(sessionManager.getUserId())
                }

                is NetworkResponse.Error -> {
                    context?.showToast("Gönderi silinemedi.")
                }
            }
        }
    }

    private fun loadPosts(posts: List<Post>) = with(binding) {
        adapter.setData(posts)
        recyclerViewUserPosts.adapter = adapter
    }

    private fun postItemClicked() {
        adapter.postItemClicked = { id ->
            val postIdBundle = Bundle().apply {
                putInt("id", id)
            }
            findNavController().navigate(
                R.id.action_navigation_profile_to_postDetailFragment,
                postIdBundle
            )
        }
    }

    private fun likeClicked() {
        adapter.likeClicked = { postId, appUserId ->
            profileViewModel.postLike(appUserId, postId)
        }
        adapter.disLikeClicked = { postId, appUserId ->
            profileViewModel.postDislike(appUserId, postId)
        }
    }

    private fun postDelete() {
        adapter.postDelete = { id ->
            val dialogBinding: PostDeleteDialogBinding =
                PostDeleteDialogBinding.inflate(layoutInflater)
            val builder = AlertDialog.Builder(requireContext()).setView(dialogBinding.root).show()
            dialogBinding.buttonSignOut.setOnClickListener {
                profileViewModel.postDelete(id)
                builder.dismiss()
            }
            dialogBinding.buttonSignOutCancel.setOnClickListener {
                builder.dismiss()
            }
        }
    }

    private fun signOut() {
        val dialogBinding: SignOutDialogBinding = SignOutDialogBinding.inflate(layoutInflater)
        val builder = AlertDialog.Builder(requireContext()).setView(dialogBinding.root).show()
        dialogBinding.buttonSignOut.setOnClickListener {
            sessionManager.setLogin(false)
            builder.dismiss()
            val intent = Intent(requireContext(), AuthenticationActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }
        dialogBinding.buttonSignOutCancel.setOnClickListener {
            builder.dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}