package com.berkayozdag.sausocial.ui.post_detail

import android.annotation.SuppressLint
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
import com.berkayozdag.sausocial.common.HomeMockData
import com.berkayozdag.sausocial.data.NetworkResponse
import com.berkayozdag.sausocial.databinding.FragmentPostDetailBinding
import com.berkayozdag.sausocial.ui.post_detail.adapters.CommentsAdapter
import com.berkayozdag.sausocial.ui.post_detail.model.Comment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PostDetailFragment : Fragment() {

    private var _binding: FragmentPostDetailBinding? = null
    private val binding get() = _binding!!
    private val adapter = CommentsAdapter()
    private var postId: Int? = null
    private val postDetailViewModel by viewModels<PostDetailViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPostDetailBinding.inflate(inflater, container, false)
        setupRecyclerview()
        setupListeners()
        setupObserves()
        onItemClick()
        postId = arguments?.getInt("id")

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postId?.let { id -> postDetailViewModel.getPostById(id) }
    }

    private fun setupListeners() = with(binding) {
        buttonBackButton.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setupObserves() = with(binding) {
        postDetailViewModel.postDetailResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResponse.Success -> {
                    // İstek başarılı oldu, veriler kullanılabilir
                    textViewUserName.text =
                        response.data[0].appUser.name + " " + response.data[0].appUser.surname
                    textViewUserDepartment.text = response.data[0].appUser.part
                    textViewPostDescription.text = response.data[0].content
                    textViewPostCreatedDate.text = response.data[0].publishedDate
                    textViewPostNumberOfLike.text = response.data[0].likeCount.toString()
                    textViewPostNumberOfComment.text = response.data[0].comments.size.toString()
                    loadComments(response.data[0].comments)
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

    private fun onItemClick() {
        adapter.onItemClicked = {

        }
    }
}