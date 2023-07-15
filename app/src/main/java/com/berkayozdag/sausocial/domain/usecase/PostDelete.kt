package com.berkayozdag.sausocial.domain.usecase

import com.berkayozdag.sausocial.common.util.NetworkResponse
import com.berkayozdag.sausocial.domain.repository.SocialAppRepository

class PostDelete(private val repository: SocialAppRepository) {
    suspend operator fun invoke(id: Int): NetworkResponse<Any> {
        return try {
            val response = repository.postDelete(id)
            NetworkResponse.Success(response)
        } catch (e: Exception) {
            NetworkResponse.Error(e.message ?: "Bir hata oluştu")
        }
    }
}