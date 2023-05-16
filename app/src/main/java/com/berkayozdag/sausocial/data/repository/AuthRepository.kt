package com.berkayozdag.sausocial.data.repository

import com.berkayozdag.sausocial.data.NetworkResponse
import com.berkayozdag.sausocial.data.api.ApiService
import com.berkayozdag.sausocial.ui.authentication.model.LoginRequest
import com.berkayozdag.sausocial.ui.authentication.model.LoginResponse
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val api: ApiService
) {

    suspend fun login(loginRequest: LoginRequest): NetworkResponse<LoginResponse> {
        return try {
            val response = api.login(loginRequest)
            NetworkResponse.Success(response)
        } catch (e: Exception) {
            NetworkResponse.Error(e.message ?: "hata")
        }
    }
}