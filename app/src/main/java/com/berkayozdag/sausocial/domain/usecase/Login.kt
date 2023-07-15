package com.berkayozdag.sausocial.domain.usecase

import com.berkayozdag.sausocial.common.util.NetworkResponse
import com.berkayozdag.sausocial.data.entities.LoginRequest
import com.berkayozdag.sausocial.data.entities.LoginResponse
import com.berkayozdag.sausocial.domain.repository.AuthRepository

class Login(private val repository: AuthRepository) {
    suspend operator fun invoke(loginRequest: LoginRequest): NetworkResponse<LoginResponse> {
        return try {
            val response = repository.login(loginRequest)
            NetworkResponse.Success(response)
        } catch (e: Exception) {
            NetworkResponse.Error(e.message ?: "Bir hata oluştu")
        }
    }
}