package com.berkayozdag.sausocial.ui.home.posts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.berkayozdag.sausocial.data.NetworkResponse
import com.berkayozdag.sausocial.data.repository.SocialAppRepository
import com.berkayozdag.sausocial.model.Like
import com.berkayozdag.sausocial.model.Post
import com.berkayozdag.sausocial.model.PostLikeRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val repository: SocialAppRepository
) : ViewModel() {

    private val _allPostResponse = MutableLiveData<NetworkResponse<List<Post>>>(null)
    val allPostResponse: LiveData<NetworkResponse<List<Post>>> = _allPostResponse

    private val _followingPosts = MutableLiveData<NetworkResponse<List<Post>>>(null)
    val followingPosts: LiveData<NetworkResponse<List<Post>>> = _followingPosts

    private val _postLikeResponse = MutableLiveData<NetworkResponse<Any>>(null)
    val postLikeResponse: LiveData<NetworkResponse<Any>> = _postLikeResponse

    fun getPosts() = viewModelScope.launch {
        _allPostResponse.postValue(NetworkResponse.Loading)
        _allPostResponse.postValue(repository.getPosts())
    }

    fun getFollowingPosts(userId: Int) = viewModelScope.launch {
        _followingPosts.postValue(NetworkResponse.Loading)
        _followingPosts.postValue(repository.getFollowingPosts(userId))
    }

    fun postLike(appUserId: Int, postId: Int) {
        viewModelScope.launch {
            val allPostResponse = _allPostResponse.value
            val followingPosts = _followingPosts.value

            if (allPostResponse is NetworkResponse.Success) {
                val newPosts = updatePostLike(allPostResponse.data, postId, appUserId)
                _allPostResponse.postValue(NetworkResponse.Success(newPosts))
                repository.postLike(PostLikeRequest(appUserId, postId))
            }

            if (followingPosts is NetworkResponse.Success) {
                val newPosts = updatePostLike(followingPosts.data, postId, appUserId)
                _followingPosts.postValue(NetworkResponse.Success(newPosts))
                repository.postLike(PostLikeRequest(appUserId, postId))
            }
        }
    }

    fun postDislike(appUserId: Int, postId: Int) {
        viewModelScope.launch {
            val allPostResponse = _allPostResponse.value
            val followingPosts = _followingPosts.value

            if (allPostResponse is NetworkResponse.Success) {
                val newPosts = updatePostDislike(allPostResponse.data, postId, appUserId)
                _allPostResponse.postValue(NetworkResponse.Success(newPosts))
                repository.postDislike(appUserId, postId)
            }

            if (followingPosts is NetworkResponse.Success) {
                val newPosts = updatePostDislike(followingPosts.data, postId, appUserId)
                _followingPosts.postValue(NetworkResponse.Success(newPosts))
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