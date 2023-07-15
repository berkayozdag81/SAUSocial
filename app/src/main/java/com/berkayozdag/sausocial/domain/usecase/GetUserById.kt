package com.berkayozdag.sausocial.domain.usecase

import com.berkayozdag.sausocial.common.util.NetworkResponse
import com.berkayozdag.sausocial.data.entities.ProfileResponse
import com.berkayozdag.sausocial.domain.repository.SocialAppRepository

class GetUserById(private val repository: SocialAppRepository) {
    suspend operator fun invoke(id: Int): NetworkResponse<ProfileResponse> {
        return try {
            val response = repository.getUserById(id)
            NetworkResponse.Success(response)
        } catch (e: Exception) {
            NetworkResponse.Error(e.message ?: "Bir hata oluştu")
        }
    }
}