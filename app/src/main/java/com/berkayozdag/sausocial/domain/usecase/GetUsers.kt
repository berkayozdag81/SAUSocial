package com.berkayozdag.sausocial.domain.usecase

import com.berkayozdag.sausocial.common.util.NetworkResponse
import com.berkayozdag.sausocial.data.entities.ProfileResponse
import com.berkayozdag.sausocial.domain.repository.SocialAppRepository

class GetUsers(private val repository: SocialAppRepository) {
    suspend operator fun invoke(): NetworkResponse<List<ProfileResponse>> {
        return try {
            val response = repository.getUsers()
            NetworkResponse.Success(response)
        } catch (e: Exception) {
            NetworkResponse.Error(e.message ?: "Bir hata oluştu")
        }
    }
}