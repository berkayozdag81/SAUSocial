package com.berkayozdag.sausocial.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.berkayozdag.sausocial.data.NetworkResponse
import com.berkayozdag.sausocial.data.repository.SocialAppRepository
import com.berkayozdag.sausocial.ui.home.Model.PostInfoItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: SocialAppRepository
) : ViewModel() {

    private val _postResponse = MutableLiveData<NetworkResponse<List<PostInfoItem>>>(null)
    val postResponse: LiveData<NetworkResponse<List<PostInfoItem>>> = _postResponse

    fun getPosts() = viewModelScope.launch {
        _postResponse.postValue(NetworkResponse.Loading)
        _postResponse.postValue(repository.getPosts())
    }
}