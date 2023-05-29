package com.berkayozdag.sausocial.ui.post_detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.berkayozdag.sausocial.data.NetworkResponse
import com.berkayozdag.sausocial.data.repository.SocialAppRepository
import com.berkayozdag.sausocial.ui.post_detail.model.PostDetailResponseItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostDetailViewModel @Inject constructor(
    private val repository: SocialAppRepository
) : ViewModel() {

    private val _postDetailResponse =
        MutableLiveData<NetworkResponse<List<PostDetailResponseItem>>>(null)
    val postDetailResponse: LiveData<NetworkResponse<List<PostDetailResponseItem>>> =
        _postDetailResponse

    fun getPostById(id: Int) = viewModelScope.launch {
        _postDetailResponse.postValue(NetworkResponse.Loading)
        _postDetailResponse.postValue(repository.getPostById(id))
    }
}