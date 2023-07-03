package com.berkayozdag.sausocial.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.berkayozdag.sausocial.data.NetworkResponse
import com.berkayozdag.sausocial.data.repository.SocialAppRepository
import com.berkayozdag.sausocial.model.FollowRequest
import com.berkayozdag.sausocial.model.Like
import com.berkayozdag.sausocial.model.Post
import com.berkayozdag.sausocial.model.PostLikeRequest
import com.berkayozdag.sausocial.model.profile.ProfileResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: SocialAppRepository
) : ViewModel() {

    private val _profileState = MutableLiveData<NetworkResponse<ProfileResponse>>()
    val profileState: LiveData<NetworkResponse<ProfileResponse>> = _profileState

    private val _followResponse = MutableLiveData<NetworkResponse<Any>>()
    val followResponse: LiveData<NetworkResponse<Any>> = _followResponse

    private val _unFollowResponse = MutableLiveData<NetworkResponse<Any>>()
    val unFollowResponse: LiveData<NetworkResponse<Any>> = _unFollowResponse

    private val _postDeleteResponse = MutableLiveData<NetworkResponse<Any>>()
    val postDeleteResponse: LiveData<NetworkResponse<Any>> = _postDeleteResponse

    fun getUserById(id: Int) = viewModelScope.launch {
        _profileState.value = NetworkResponse.Loading
        _profileState.value = repository.getUserById(id)
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

    fun postDelete(postId: Int) = viewModelScope.launch {
        _postDeleteResponse.value = NetworkResponse.Loading
        _postDeleteResponse.value = repository.postDelete(postId)
    }

    fun postLike(appUserId: Int, postId: Int) {
        viewModelScope.launch {
            val profileData = _profileState.value
            if (profileData is NetworkResponse.Success) {
                val newPosts = updatePostLike(profileData.data.posts, postId, appUserId)
                _profileState.postValue(NetworkResponse.Success(profileData.data.copy(posts = newPosts)))
                repository.postLike(PostLikeRequest(appUserId, postId))
            }
        }
    }

    fun postDislike(appUserId: Int, postId: Int) {
        viewModelScope.launch {
            val profileData = _profileState.value
            if (profileData is NetworkResponse.Success) {
                val newPosts = updatePostDislike(profileData.data.posts, postId, appUserId)
                _profileState.postValue(NetworkResponse.Success(profileData.data.copy(posts = newPosts)))
                repository.postDislike(appUserId, postId)
            }
        }
    }

    private fun updatePostLike(posts: List<Post>, postId: Int, appUserId: Int): List<Post> {
        return posts.map { post ->
            if (postId == post.id)
                post.copy(likes = post.likes.plus(Like(appUserId, postId)))
            else post
        }
    }

    private fun updatePostDislike(posts: List<Post>, postId: Int, appUserId: Int): List<Post> {
        return posts.map { post ->
            if (postId == post.id)
                post.copy(likes = post.likes.filter { like -> like.appUserId != appUserId })
            else post
        }
    }

}