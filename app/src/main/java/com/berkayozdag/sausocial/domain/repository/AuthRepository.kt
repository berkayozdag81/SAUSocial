package com.berkayozdag.sausocial.domain.repository

import com.berkayozdag.sausocial.data.entities.LoginRequest
import com.berkayozdag.sausocial.data.entities.LoginResponse

interface AuthRepository {
    suspend fun login(loginRequest: LoginRequest): LoginResponse
}