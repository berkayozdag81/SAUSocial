package com.berkayozdag.sausocial.ui.post_detail

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.berkayozdag.sausocial.R
import com.berkayozdag.sausocial.common.*
import com.berkayozdag.sausocial.common.util.Constants
import com.berkayozdag.sausocial.common.util.NetworkResponse
import com.berkayozdag.sausocial.common.util.loadImage
import com.berkayozdag.sausocial.common.util.setVisible
import com.berkayozdag.sausocial.common.util.showToast
import com.berkayozdag.sausocial.data.entities.Comment
import com.berkayozdag.sausocial.databinding.FragmentPostDetailBinding
import com.berkayozdag.sausocial.ui.post_detail.adapters.CommentsAdapter
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class PostDetailFragment : Fragment() {

    private var _binding: FragmentPostDetailBinding? = null
    private val binding get() = _binding!!
    private val adapter = CommentsAdapter()
    private var postId: Int? = null
    private var userId: Int? = null
    private val postDetailViewModel by viewModels<PostDetailViewModel>()

    @RequiresApi(Build.VERSION_CODES.N)
    private val dateFormat =
        SimpleDateFormat("dd.MM.yyyy hh:mm:ss", Locale.getDefault(Locale.Category.FORMAT))
    private val currentDate = Date()

    @Inject
    lateinit var sessionManager: SessionManager
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPostDetailBinding.inflate(inflater, container, false)
        binding.imageViewUserComment.loadImage(sessionManager.getUserProfileImage())
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postId = arguments?.getInt("id")
        userItemClicked()
        setupRecyclerview()
        setupListeners()
        postId?.let { id -> postDetailViewModel.getPostById(id) }
        setupObserves()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun setupListeners() = with(binding) {
        buttonPostLike.setOnClickListener {
            postId?.let { id -> postDetailViewModel.postLike(sessionManager.getUserId(), id) }
        }

        buttonPostDislike.setOnClickListener {
            postId?.let { id -> postDetailViewModel.postDisLike(sessionManager.getUserId(), id) }
        }

        buttonBackButton.setOnClickListener {
            findNavController().popBackStack()
        }

        buttonSendComment.setOnClickListener {
            postId?.let { postId ->
                postDetailViewModel.sendComment(
                    editTextComment.text.toString(),
                    dateFormat.format(currentDate),
                    postId,
                    sessionManager.getUserId()
                )
            }
        }

        swipeRefreshLayout.setOnRefreshListener {
            swipeRefreshLayout.isRefreshing = false
            postId?.let { id -> postDetailViewModel.getPostById(id) }
        }

        imageViewUserProfile.setOnClickListener {
            if (sessionManager.getUserId() == userId) {
                findNavController().navigate(
                    R.id.action_postDetailFragment_to_navigation_profile,
                )
            } else {
                val userIdBundle = Bundle()
                userId?.let { userId -> userIdBundle.putInt("id", userId) }
                findNavController().navigate(
                    R.id.action_postDetailFragment_to_otherProfileFragment,
                    userIdBundle
                )
            }
        }

        buttonFollow.setOnClickListener {
            userId?.let { id -> postDetailViewModel.follow(sessionManager.getUserId(), id) }
        }

        buttonUnFollow.setOnClickListener {
            userId?.let { id -> postDetailViewModel.unFollow(sessionManager.getUserId(), id) }
        }
    }

    private fun userItemClicked() {
        adapter.userItemClicked = { id ->
            if (sessionManager.getUserId() == id) {
                findNavController().navigate(
                    R.id.action_postDetailFragment_to_navigation_profile,
                )
            } else {
                val userIdBundle = Bundle().apply {
                    putInt("id", id)
                }
                findNavController().navigate(
                    R.id.action_postDetailFragment_to_otherProfileFragment,
                    userIdBundle
                )
            }
        }
    }

    private fun setupObserves() {
        postDetailViewModel.postDetailResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResponse.Success -> {
                    with(binding) {
                        val appUser = response.data.appUser

                        textViewUserName.text = "${appUser.name} ${appUser.surname}"
                        textViewUserDepartment.text = appUser.part
                        textViewPostDescription.text = response.data.content
                        textViewPostCreatedDate.text = response.data.publishedDate
                        textViewPostNumberOfLike.text = response.data.likes.size.toString()
                        textViewPostNumberOfComment.text = response.data.comments.size.toString()
                        userId = appUser.id
                        binding.layoutNoResult.root.setVisible(response.data.comments.isEmpty())
                        postDetailProgressBar.setVisible(false)
                        appUser.profileImageUrl.let { url ->
                            imageViewUserProfile.loadImage(Constants.API_USER_IMAGES_URL + url)
                        }

                        textViewUserDepartment.setVisible(!appUser.part.isNullOrEmpty())

                        val isCurrentUser = response.data.appUser.id == sessionManager.getUserId()
                        val isFollowing =
                            response.data.appUser.followers.any { it.followerId == sessionManager.getUserId() }

                        buttonFollow.setVisible(!isCurrentUser && !isFollowing)
                        buttonUnFollow.setVisible(!isCurrentUser && isFollowing)

                        val isCurrentUserLikedPost =
                            response.data.isUserLikedThisPost(sessionManager.getUserId())
                        buttonPostLike.setVisible(!isCurrentUserLikedPost)
                        buttonPostDislike.setVisible(isCurrentUserLikedPost)
                    }

                    loadComments(response.data.comments)
                }

                is NetworkResponse.Error -> {
                    with(binding) {
                        postDetailProgressBar.setVisible(false)
                        context?.showToast(response.errorMessage)
                    }
                }

                NetworkResponse.Loading -> {
                    binding.postDetailProgressBar.setVisible(true)
                }
            }
        }

        postDetailViewModel.followResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResponse.Loading -> {}

                is NetworkResponse.Success -> {
                    with(binding) {
                        buttonUnFollow.setVisible(true)
                        buttonFollow.setVisible(false)
                    }
                }

                is NetworkResponse.Error -> {
                    context?.showToast("Kullanıcı takip edilemedi.")
                }
            }
        }

        postDetailViewModel.unFollowResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResponse.Loading -> {}

                is NetworkResponse.Success -> {
                    with(binding) {
                        buttonUnFollow.setVisible(false)
                        buttonFollow.setVisible(true)
                    }
                }

                is NetworkResponse.Error -> {
                    context?.showToast("Kullanıcı takipten çıkarılamadı.")
                }
            }
        }

        postDetailViewModel.postLikeResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResponse.Loading -> {}
                is NetworkResponse.Success -> {
                    with(binding) {
                        val likeCount = textViewPostNumberOfLike.text.toString().toInt() + 1
                        buttonPostLike.setVisible(false)
                        buttonPostDislike.setVisible(true)
                        textViewPostNumberOfLike.text = likeCount.toString()
                    }
                }

                is NetworkResponse.Error -> {
                    context?.showToast("Gönderi beğenilemedi.")
                }
            }
        }

        postDetailViewModel.postDislikeResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResponse.Loading -> {}
                is NetworkResponse.Success -> {
                    with(binding) {
                        val likeCount = textViewPostNumberOfLike.text.toString().toInt() - 1
                        buttonPostLike.setVisible(true)
                        buttonPostDislike.setVisible(false)
                        textViewPostNumberOfLike.text = likeCount.toString()
                    }
                }

                is NetworkResponse.Error -> {
                    context?.showToast("Gönderi beğenilerden çıkarılamadı.")
                }
            }
        }

        postDetailViewModel.commentCreateResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResponse.Success -> {
                    with(binding) {
                        postDetailProgressBar.setVisible(false)
                        postId?.let { id -> postDetailViewModel.getPostById(id) }
                        editTextComment.text.clear()
                        recyclerViewPostComments.scrollToPosition(adapter.itemCount-1)
                    }
                }

                is NetworkResponse.Error -> {
                    binding.postDetailProgressBar.setVisible(false)
                    context?.showToast("Yorum paylaşılamadı.")
                }

                NetworkResponse.Loading -> {
                    binding.postDetailProgressBar.setVisible(true)
                }
            }
        }
    }

    private fun setupRecyclerview() = with(binding) {
        val layoutManager = LinearLayoutManager(requireContext())
        val itemDecoration: RecyclerView.ItemDecoration = DividerItemDecoration(
            context,
            DividerItemDecoration.VERTICAL
        )
        recyclerViewPostComments.addItemDecoration(itemDecoration)
        recyclerViewPostComments.layoutManager = layoutManager
    }

    private fun loadComments(commentEntities: List<Comment>) = with(binding) {
        adapter.setData(commentEntities)
        recyclerViewPostComments.adapter = adapter
    }

}