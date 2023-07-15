package com.berkayozdag.sausocial.domain.usecase

import com.berkayozdag.sausocial.common.util.NetworkResponse
import com.berkayozdag.sausocial.domain.repository.SocialAppRepository

class UnFollow(private val repository: SocialAppRepository) {
    suspend operator fun invoke(followerId: Int, userId: Int): NetworkResponse<Any> {
        return try {
            val response = repository.unFollow(followerId,userId)
            NetworkResponse.Success(response)
        } catch (e: Exception) {
            NetworkResponse.Error(e.message ?: "Bir hata oluştu")
        }
    }
}