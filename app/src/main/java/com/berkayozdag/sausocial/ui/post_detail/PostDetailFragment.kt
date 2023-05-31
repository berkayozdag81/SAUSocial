package com.berkayozdag.sausocial.ui.post_detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.berkayozdag.sausocial.common.SessionManager
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
    private val postDetailViewModel by viewModels<PostDetailViewModel>()

    val dateFormat = SimpleDateFormat("dd.MM.yyyy")
    val currentDate = Date()

    @Inject
    lateinit var sessionManager: SessionManager
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPostDetailBinding.inflate(inflater, container, false)
        setupRecyclerview()
        setupListeners()
        setupObserves()
        postId = arguments?.getInt("id")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postId?.let { id -> postDetailViewModel.getPostById(id) }
    }

    private fun setupListeners() = with(binding) {
        val formattedDate = dateFormat.format(currentDate)

        buttonBackButton.setOnClickListener {
            findNavController().popBackStack()
        }

        buttonSendComment.setOnClickListener {
            postId?.let { postId ->
                postDetailViewModel.sendComment(
                    editTextComment.text.toString(),
                    formattedDate,
                    postId,
                    sessionManager.getUserId()
                )
            }
        }
    }

    private fun setupObserves() = with(binding) {
        postDetailViewModel.postDetailResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResponse.Success -> {
                    // İstek başarılı oldu, veriler kullanılabilir
                    textViewUserName.text =
                        response.data.appUser.name + " " + response.data.appUser.surname
                    textViewUserDepartment.text = response.data.appUser.part
                    textViewPostDescription.text = response.data.content
                    textViewPostCreatedDate.text = response.data.publishedDate
                    textViewPostNumberOfLike.text = response.data.likeCount.toString()
                    textViewPostNumberOfComment.text = response.data.comments.size.toString()
                    loadComments(response.data.comments)
                }
                is NetworkResponse.Error -> {
                    // İstekte bir hata oluştu
                    val errorMessage = response.errorMessage
                    //Toast.makeText(this., errorMessage, Toast.LENGTH_LONG).show()
                }
                NetworkResponse.Loading -> {
                    // Yükleme animasyonu vb. gösterilebilir
                }
            }
        }

        postDetailViewModel.commentCreateResponse.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResponse.Success -> {
                    // İstek başarılı oldu, veriler kullanılabilir
                    Toast.makeText(requireContext(), "Yorum paylaşıldı.", Toast.LENGTH_LONG).show()
                    postId?.let { id -> postDetailViewModel.getPostById(id) }
                    editTextComment.text.clear()
                }
                is NetworkResponse.Error -> {
                    // İstekte bir hata oluştu
                    Toast.makeText(requireContext(), "Hata", Toast.LENGTH_LONG).show()
                }
                NetworkResponse.Loading -> {
                    // Yükleme animasyonu vb. gösterilebilir
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