package com.berkayozdag.sausocial.domain.usecase

import com.berkayozdag.sausocial.common.util.NetworkResponse
import com.berkayozdag.sausocial.domain.repository.SocialAppRepository

class PostDisLike(private val repository: SocialAppRepository) {
    suspend operator fun invoke(appUserId: Int, postId: Int): NetworkResponse<Any> {
        return try {
            val response = repository.postDisLike(appUserId, postId)
            NetworkResponse.Success(response)
        } catch (e: Exception) {
            NetworkResponse.Error(e.message ?: "Bir hata oluştu")
        }
    }
}