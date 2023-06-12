package com.berkayozdag.sausocial.ui.post_detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.berkayozdag.sausocial.data.NetworkResponse
import com.berkayozdag.sausocial.data.repository.SocialAppRepository
import com.berkayozdag.sausocial.model.CommentRequest
import com.berkayozdag.sausocial.model.CommentResponse
import com.berkayozdag.sausocial.model.FollowRequest
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

    private val _followResponse = MutableLiveData<NetworkResponse<Any>>()
    val followResponse: LiveData<NetworkResponse<Any>> = _followResponse

    private val _unFollowResponse = MutableLiveData<NetworkResponse<Any>>()
    val unFollowResponse: LiveData<NetworkResponse<Any>> = _unFollowResponse

    fun sendComment(message: String, publishedDate: String, postId: Int, appUserId: Int) =
        viewModelScope.launch {
            _commentCreateResponse.value = NetworkResponse.Loading
            _commentCreateResponse.value =
                repository.sendComment(CommentRequest(message, publishedDate, postId, appUserId))
        }


    fun follow(followerId: Int, userId: Int) = viewModelScope.launch {
        _followResponse.value = NetworkResponse.Loading
        _followResponse.value =
            repository.follow(FollowRequest(followerId, userId))
    }

    fun unFollow(followerId: Int, userId: Int) = viewModelScope.launch {
        _unFollowResponse.value = NetworkResponse.Loading
        _unFollowResponse.value =
            repository.unFollow(followerId, userId)
    }
}