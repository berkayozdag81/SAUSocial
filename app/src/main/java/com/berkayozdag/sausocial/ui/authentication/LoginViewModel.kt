package com.berkayozdag.sausocial.ui.authentication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.berkayozdag.sausocial.common.util.NetworkResponse
import com.berkayozdag.sausocial.data.entities.LoginRequest
import com.berkayozdag.sausocial.data.entities.LoginResponse
import com.berkayozdag.sausocial.domain.usecase.AuthUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authUseCases: AuthUseCases
) : ViewModel() {

    private val _loginState = MutableLiveData<NetworkResponse<LoginResponse>>()
    val loginState: LiveData<NetworkResponse<LoginResponse>> = _loginState

    fun login(userName: String, password: String) {
        viewModelScope.launch {
            _loginState.value = NetworkResponse.Loading
            _loginState.value = authUseCases.login(LoginRequest(userName,password))
        }
    }

}
