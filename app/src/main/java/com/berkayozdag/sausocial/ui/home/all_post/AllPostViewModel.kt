package com.berkayozdag.sausocial.ui.home.all_post

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
class AllPostViewModel @Inject constructor(
    private val repository: SocialAppRepository
) : ViewModel() {

    private val _postResponse = MutableLiveData<NetworkResponse<List<Post>>>(null)
    val postResponse: LiveData<NetworkResponse<List<Post>>> = _postResponse

    private val _postLikeResponse = MutableLiveData<NetworkResponse<Any>>(null)
    val postLikeResponse: LiveData<NetworkResponse<Any>> = _postLikeResponse

    fun getPosts() = viewModelScope.launch {
        _postResponse.postValue(NetworkResponse.Loading)
        _postResponse.postValue(repository.getPosts())
    }

    fun postLike(appUserId: Int, postId: Int) {
        viewModelScope.launch {
            when (val postResponse = _postResponse.value) {
                is NetworkResponse.Success -> {
                    val newPosts = postResponse.data.map {
                        if (postId == it.id)
                            it.copy(likes = it.likes.plus(Like(appUserId,postId)))
                        else it
                    }
                    _postResponse.postValue(NetworkResponse.Success(newPosts))
                    repository.postLike(PostLikeRequest(appUserId, postId))
                }

                else -> Unit
            }
        }
    }

    fun postDislike(appUserId: Int, postId: Int) {
        viewModelScope.launch {
            when (val postResponse = _postResponse.value) {
                is NetworkResponse.Success -> {
                    val newPosts = postResponse.data.map { it ->
                        if (postId == it.id)
                            it.copy(likes = it.likes.filter { it.appUserId != appUserId })
                        else it
                    }
                    _postResponse.postValue(NetworkResponse.Success(newPosts))
                    repository.postDislike(appUserId, postId)
                }

                else -> Unit
            }
        }
    }
}