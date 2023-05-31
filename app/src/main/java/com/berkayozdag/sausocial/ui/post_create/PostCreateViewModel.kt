package com.berkayozdag.sausocial.ui.post_create

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.berkayozdag.sausocial.data.NetworkResponse
import com.berkayozdag.sausocial.data.repository.SocialAppRepository
import com.berkayozdag.sausocial.model.PostRequest
import com.berkayozdag.sausocial.model.PostResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostCreateViewModel @Inject constructor(
    private val repository: SocialAppRepository
) : ViewModel() {

    private val _postCreateResponse = MutableLiveData<NetworkResponse<PostResponse>>()
    val postCreateResponse: LiveData<NetworkResponse<PostResponse>> = _postCreateResponse

    fun sendPost(content: String, publishedDate: String, likeCount: Int, appUserId: Int) =
        viewModelScope.launch {
            _postCreateResponse.value = NetworkResponse.Loading
            _postCreateResponse.value =
                repository.sendPost(PostRequest(content, publishedDate, likeCount, appUserId))
        }

}