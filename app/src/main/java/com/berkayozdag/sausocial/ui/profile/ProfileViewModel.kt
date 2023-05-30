package com.berkayozdag.sausocial.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.berkayozdag.sausocial.data.NetworkResponse
import com.berkayozdag.sausocial.data.repository.SocialAppRepository
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

    fun getUserById(id: Int) = viewModelScope.launch {
        _profileState.value = NetworkResponse.Loading
        _profileState.value = repository.getUserById(id)
    }

}