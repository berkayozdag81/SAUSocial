package com.berkayozdag.sausocial.ui.groups

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
class GroupsViewModel @Inject constructor(
    private val repository: SocialAppRepository
) : ViewModel() {

    private val _usersResponse = MutableLiveData<NetworkResponse<List<ProfileResponse>>>()
    val usersResponse: LiveData<NetworkResponse<List<ProfileResponse>>> = _usersResponse

    fun getUsers() = viewModelScope.launch {
        _usersResponse.value = NetworkResponse.Loading
        _usersResponse.value = repository.getUsers()
    }

}