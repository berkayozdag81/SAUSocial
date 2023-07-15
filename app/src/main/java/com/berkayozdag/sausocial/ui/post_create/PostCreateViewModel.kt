package com.berkayozdag.sausocial.ui.post_create

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.berkayozdag.sausocial.common.util.NetworkResponse
import com.berkayozdag.sausocial.data.entities.PostRequest
import com.berkayozdag.sausocial.data.entities.PostResponse
import com.berkayozdag.sausocial.domain.usecase.SocialAppUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostCreateViewModel @Inject constructor(
    private val socialAppUseCases: SocialAppUseCases
) : ViewModel() {

    private val _postCreateResponse = MutableLiveData<NetworkResponse<PostResponse>>()
    val postCreateResponse: LiveData<NetworkResponse<PostResponse>> = _postCreateResponse

    fun sendPost(content: String, publishedDate: String, likeCount: Int, appUserId: Int) =
        viewModelScope.launch {
            _postCreateResponse.value = NetworkResponse.Loading
            _postCreateResponse.value =
                socialAppUseCases.sendPost(PostRequest(content, publishedDate, likeCount, appUserId))
        }

}