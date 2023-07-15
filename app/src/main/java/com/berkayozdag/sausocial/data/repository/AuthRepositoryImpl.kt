package com.berkayozdag.sausocial.data.repository

import com.berkayozdag.sausocial.data.api.SocialAppApi
import com.berkayozdag.sausocial.data.entities.LoginRequest
import com.berkayozdag.sausocial.data.entities.LoginResponse
import com.berkayozdag.sausocial.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(private val api: SocialAppApi) : AuthRepository {
    override suspend fun login(loginRequest: LoginRequest): LoginResponse {
        return api.login(loginRequest)
    }

}