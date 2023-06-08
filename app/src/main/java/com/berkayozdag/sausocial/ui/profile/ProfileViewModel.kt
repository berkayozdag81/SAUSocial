package com.berkayozdag.sausocial.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.berkayozdag.sausocial.data.NetworkResponse
import com.berkayozdag.sausocial.data.repository.SocialAppRepository
import com.berkayozdag.sausocial.model.CommentRequest
import com.berkayozdag.sausocial.model.CommentResponse
import com.berkayozdag.sausocial.model.FollowRequest
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
}