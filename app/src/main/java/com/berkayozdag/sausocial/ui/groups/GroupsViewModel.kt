package com.berkayozdag.sausocial.ui.groups

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.berkayozdag.sausocial.common.util.NetworkResponse
import com.berkayozdag.sausocial.data.entities.ProfileResponse
import com.berkayozdag.sausocial.domain.usecase.SocialAppUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroupsViewModel @Inject constructor(
    private val socialAppUseCases: SocialAppUseCases
) : ViewModel() {

    private val _usersResponse = MutableLiveData<NetworkResponse<List<ProfileResponse>>>()
    val usersResponse: LiveData<NetworkResponse<List<ProfileResponse>>> = _usersResponse

    fun getUsers() = viewModelScope.launch {
        _usersResponse.value = NetworkResponse.Loading
        _usersResponse.value = socialAppUseCases.getUsers()
    }

}