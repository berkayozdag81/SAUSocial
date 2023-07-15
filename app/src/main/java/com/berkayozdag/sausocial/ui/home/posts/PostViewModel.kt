package com.berkayozdag.sausocial.ui.home.posts

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.berkayozdag.sausocial.common.util.NetworkResponse
import com.berkayozdag.sausocial.data.entities.Like
import com.berkayozdag.sausocial.data.entities.Post
import com.berkayozdag.sausocial.data.entities.PostLikeRequest
import com.berkayozdag.sausocial.domain.usecase.SocialAppUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val socialAppUseCases: SocialAppUseCases
) : ViewModel() {

    private val _allPostResponseEntity = MutableLiveData<NetworkResponse<List<Post>>>(null)
    val allPostResponseEntity: LiveData<NetworkResponse<List<Post>>> = _allPostResponseEntity

    private val _followingPosts = MutableLiveData<NetworkResponse<List<Post>>>(null)
    val followingPosts: LiveData<NetworkResponse<List<Post>>> = _followingPosts

    private val _postLikeResponse = MutableLiveData<NetworkResponse<Any>>(null)
    val postLikeResponse: LiveData<NetworkResponse<Any>> = _postLikeResponse

    @RequiresApi(Build.VERSION_CODES.N)
    fun getPosts() = viewModelScope.launch {
        _allPostResponseEntity.postValue(NetworkResponse.Loading)
        _allPostResponseEntity.postValue(socialAppUseCases.getPosts())
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun getFollowingPosts(userId: Int) = viewModelScope.launch {
        _followingPosts.postValue(NetworkResponse.Loading)
        _followingPosts.postValue(socialAppUseCases.getFollowingPosts(userId))
    }

    fun postLike(appUserId: Int, postId: Int) {
        viewModelScope.launch {
            val allPostResponse = _allPostResponseEntity.value
            val followingPosts = _followingPosts.value

            if (allPostResponse is NetworkResponse.Success) {
                val newPosts = updatePostLike(allPostResponse.data, postId, appUserId)
                _allPostResponseEntity.postValue(NetworkResponse.Success(newPosts))
                socialAppUseCases.postLike(PostLikeRequest(appUserId, postId))
            }

            if (followingPosts is NetworkResponse.Success) {
                val newPosts = updatePostLike(followingPosts.data, postId, appUserId)
                _followingPosts.postValue(NetworkResponse.Success(newPosts))
                socialAppUseCases.postLike(PostLikeRequest(appUserId, postId))
            }
        }
    }

    fun postDislike(appUserId: Int, postId: Int) {
        viewModelScope.launch {
            val allPostResponse = _allPostResponseEntity.value
            val followingPosts = _followingPosts.value

            if (allPostResponse is NetworkResponse.Success) {
                val newPosts = updatePostDislike(allPostResponse.data, postId, appUserId)
                _allPostResponseEntity.postValue(NetworkResponse.Success(newPosts))
                socialAppUseCases.postDisLike(appUserId, postId)
            }

            if (followingPosts is NetworkResponse.Success) {
                val newPosts = updatePostDislike(followingPosts.data, postId, appUserId)
                _followingPosts.postValue(NetworkResponse.Success(newPosts))
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