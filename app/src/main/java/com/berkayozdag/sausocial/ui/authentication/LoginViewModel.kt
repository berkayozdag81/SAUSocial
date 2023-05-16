package com.berkayozdag.sausocial.ui.authentication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.berkayozdag.sausocial.data.NetworkResponse
import com.berkayozdag.sausocial.data.repository.AuthRepository
import com.berkayozdag.sausocial.data.repository.SocialAppRepository
import com.berkayozdag.sausocial.ui.authentication.model.LoginRequest
import com.berkayozdag.sausocial.ui.authentication.model.LoginResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    private val _loginState = MutableLiveData<NetworkResponse<LoginResponse>>()
    val loginState: LiveData<NetworkResponse<LoginResponse>> = _loginState

    fun login(userName: String, password: String) {
        viewModelScope.launch {
            _loginState.value = NetworkResponse.Loading
            _loginState.value = repository.login(LoginRequest(userName, password))
        }
    }
}
