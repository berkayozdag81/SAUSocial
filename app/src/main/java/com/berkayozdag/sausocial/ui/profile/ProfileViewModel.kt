package com.berkayozdag.sausocial.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.berkayozdag.sausocial.common.util.NetworkResponse
import com.berkayozdag.sausocial.data.entities.FollowRequest
import com.berkayozdag.sausocial.data.entities.Like
import com.berkayozdag.sausocial.data.entities.Post
import com.berkayozdag.sausocial.data.entities.PostLikeRequest
import com.berkayozdag.sausocial.data.entities.ProfileResponse
import com.berkayozdag.sausocial.domain.usecase.SocialAppUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val socialAppUseCases: SocialAppUseCases
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
        _profileState.value = socialAppUseCases.getUserById(id)
    }

    fun follow(followerId: Int, userId: Int) = viewModelScope.launch {
        _followResponse.value = NetworkResponse.Loading
        _followResponse.value =
            socialAppUseCases.follow(FollowRequest(followerId, userId))
    }

    fun unFollow(followerId: Int, userId: Int) = viewModelScope.launch {
        _unFollowResponse.value = NetworkResponse.Loading
        _unFollowResponse.value =
            socialAppUseCases.unFollow(followerId, userId)
    }

    fun postDelete(postId: Int) = viewModelScope.launch {
        _postDeleteResponse.value = NetworkResponse.Loading
        _postDeleteResponse.value = socialAppUseCases.postDelete(postId)
    }

    fun postLike(appUserId: Int, postId: Int) {
        viewModelScope.launch {
            val profileData = _profileState.value
            if (profileData is NetworkResponse.Success) {
                val newPosts = updatePostLike(profileData.data.posts, postId, appUserId)
                _profileState.postValue(NetworkResponse.Success(profileData.data.copy(posts = newPosts)))
                socialAppUseCases.postLike(PostLikeRequest(appUserId, postId))
            }
        }
    }

    fun postDislike(appUserId: Int, postId: Int) {
        viewModelScope.launch {
            val profileData = _profileState.value
            if (profileData is NetworkResponse.Success) {
                val newPosts = updatePostDislike(profileData.data.posts, postId, appUserId)
                _profileState.postValue(NetworkResponse.Success(profileData.data.copy(posts = newPosts)))
                socialAppUseCases.postDisLike(appUserId, postId)
            }
        }
    }

    private fun updatePostLike(postEntities: List<Post>, postId: Int, appUserId: Int): List<Post> {
        return postEntities.map { post ->
            if (postId == post.id)
                post.copy(likes = post.likes.plus(Like(appUserId, postId)))
            else post
        }
    }

    private fun updatePostDislike(postEntities: List<Post>, postId: Int, appUserId: Int): List<Post> {
        return postEntities.map { post ->
            if (postId == post.id)
                post.copy(likes = post.likes.filter { like -> like.appUserId != appUserId })
            else post
        }
    }

}