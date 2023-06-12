package com.berkayozdag.sausocial.ui.post_detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
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
import com.berkayozdag.sausocial.databinding.FragmentPostDetailBinding
import com.berkayozdag.sausocial.model.Comment
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
    private val dateFormat = SimpleDateFormat("dd.MM.yyyy hh:mm:ss")
    private val currentDate = Date()

    @Inject
    lateinit var sessionManager: SessionManager
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPostDetailBinding.inflate(inflater, container, false)
        postId = arguments?.getInt("id")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userItemClicked()
        setupRecyclerview()
        setupListeners()
        postId?.let { id -> postDetailViewModel.getPostById(id) }
        setupObserves()
    }

    private fun setupListeners() = with(binding) {
        buttonPostLike.setOnClickListener {
            postId?.let { it1 -> postDetailViewModel.postLike(sessionManager.getUserId(), it1) }
        }

        buttonPostdisLike.setOnClickListener {
            postId?.let { it1 -> postDetailViewModel.postDisLike(sessionManager.getUserId(), it1) }
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
            postId?.let { postId -> postDetailViewModel.getPostById(postId) }
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
            userId?.let { it1 -> postDetailViewModel.follow(sessionManager.getUserId(), it1) }
        }

        buttonUnFollow.setOnClickListener {
            userId?.let { it1 -> postDetailViewModel.unFollow(sessionManager.getUserId(), it1) }
        }
    }

    private fun userItemClicked() {
        adapter.userItemClicked = { id ->
            if (sessionManager.getUserId() == id) {
                findNavController().navigate(
                    R.id.action_postDetailFragment_to_navigation_profile,
                )
            } else {
                val userIdBundle = Bundle()
                userIdBundle.putInt("id", id)
                findNavController().navigate(
                    R.id.action_postDetailFragment_to_otherProfileFragment,
                    userIdBundle
                )
            }
        }
    }

    private fun setupObserves() = with(binding) {
        postDetailViewModel.postDetailResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResponse.Success -> {
                    textViewUserName.text =
                        response.data.appUser.name + " " + response.data.appUser.surname
                    textViewUserDepartment.text = response.data.appUser.part
                    textViewPostDescription.text = response.data.content
                    textViewPostCreatedDate.text = response.data.publishedDate
                    textViewPostNumberOfLike.text = response.data.likes.size.toString()
                    textViewPostNumberOfComment.text = response.data.comments.size.toString()
                    userId = response.data.appUser.id
                    binding.layoutNoResult.root.setVisible(response.data.comments.isEmpty())
                    postDetailProgressBar.setVisible(false)
                    if (response.data.appUser.followers.any { it.followerId == sessionManager.getUserId() }) {
                        buttonFollow.setVisible(false)
                        buttonUnFollow.setVisible(true)
                    } else {
                        buttonFollow.setVisible(true)
                        buttonUnFollow.setVisible(false)
                    }

                    if (response.data.appUser.id == sessionManager.getUserId()) {
                        buttonFollow.setVisible(false)
                        buttonUnFollow.setVisible(false)
                    }

                    if (response.data.isUserLikedThisPost(sessionManager.getUserId())) {
                        buttonPostLike.setVisible(false)
                        buttonPostdisLike.setVisible(true)
                    }else{
                        buttonPostLike.setVisible(true)
                        buttonPostdisLike.setVisible(false)
                    }

                    loadComments(response.data.comments)
                }

                is NetworkResponse.Error -> {
                    postDetailProgressBar.setVisible(false)
                    context?.showToast(response.errorMessage)
                }

                NetworkResponse.Loading -> {
                    postDetailProgressBar.setVisible(true)
                }
            }
        }

        postDetailViewModel.followResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResponse.Loading -> {
                }

                is NetworkResponse.Success -> {
                    binding.buttonUnFollow.setVisible(true)
                    binding.buttonFollow.setVisible(false)
                }

                is NetworkResponse.Error -> {
                    context?.showToast("İstek başarısız")
                }
            }
        }

        postDetailViewModel.unFollowResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResponse.Loading -> {
                }

                is NetworkResponse.Success -> {
                    binding.buttonUnFollow.setVisible(false)
                    binding.buttonFollow.setVisible(true)
                }

                is NetworkResponse.Error -> {
                    context?.showToast("İstek başarısız")
                }
            }
        }

        postDetailViewModel.postLikeResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResponse.Loading -> {}
                is NetworkResponse.Success -> {
                    val likeCount = binding.textViewPostNumberOfLike.text.toString().toInt() + 1
                    buttonPostLike.setVisible(false)
                    buttonPostdisLike.setVisible(true)
                    binding.textViewPostNumberOfLike.text = likeCount.toString()
                }

                is NetworkResponse.Error -> {
                    context?.showToast("İstek Başarısız")
                }
            }
        }

        postDetailViewModel.postDislikeResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResponse.Loading -> {}
                is NetworkResponse.Success -> {
                    val likeCount = binding.textViewPostNumberOfLike.text.toString().toInt() - 1
                    buttonPostLike.setVisible(true)
                    buttonPostdisLike.setVisible(false)
                    binding.textViewPostNumberOfLike.text = likeCount.toString()
                }

                is NetworkResponse.Error -> {
                    context?.showToast("İstek Başarısız")
                }
            }
        }

        postDetailViewModel.commentCreateResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResponse.Success -> {
                    context?.showToast("Yorum paylaşıldı.")
                    postDetailProgressBar.setVisible(false)
                    postId?.let { id -> postDetailViewModel.getPostById(id) }
                    editTextComment.text.clear()
                }

                is NetworkResponse.Error -> {
                    postDetailProgressBar.setVisible(false)
                    context?.showToast(response.errorMessage)
                }

                NetworkResponse.Loading -> {
                    postDetailProgressBar.setVisible(true)
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

    private fun loadComments(comments: List<Comment>) = with(binding) {
        adapter.setData(comments)
        recyclerViewPostComments.adapter = adapter
    }

}