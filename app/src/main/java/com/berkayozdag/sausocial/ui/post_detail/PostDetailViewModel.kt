package com.berkayozdag.sausocial.ui.post_detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.berkayozdag.sausocial.data.NetworkResponse
import com.berkayozdag.sausocial.data.repository.SocialAppRepository
import com.berkayozdag.sausocial.model.CommentRequest
import com.berkayozdag.sausocial.model.CommentResponse
import com.berkayozdag.sausocial.model.Post
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostDetailViewModel @Inject constructor(
    private val repository: SocialAppRepository
) : ViewModel() {

    private val _postDetailResponse =
        MutableLiveData<NetworkResponse<Post>>(null)
    val postDetailResponse: LiveData<NetworkResponse<Post>> =
        _postDetailResponse

    fun getPostById(id: Int) = viewModelScope.launch {
        _postDetailResponse.postValue(NetworkResponse.Loading)
        _postDetailResponse.postValue(repository.getPostById(id))
    }

    private val _commentCreateResponse = MutableLiveData<NetworkResponse<CommentResponse>>()
    val commentCreateResponse: LiveData<NetworkResponse<CommentResponse>> = _commentCreateResponse

    fun sendComment(message: String, publishedDate: String, postId: Int, appUserId: Int) =
        viewModelScope.launch {
            _commentCreateResponse.value = NetworkResponse.Loading
            _commentCreateResponse.value =
                repository.sendComment(CommentRequest(message, publishedDate, postId, appUserId))
        }
}