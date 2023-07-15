package com.berkayozdag.sausocial.domain.usecase

import com.berkayozdag.sausocial.common.util.NetworkResponse
import com.berkayozdag.sausocial.data.entities.PostLikeRequest
import com.berkayozdag.sausocial.domain.repository.SocialAppRepository

class PostLike(private val repository: SocialAppRepository) {
    suspend operator fun invoke(postLikeRequest: PostLikeRequest): NetworkResponse<Any> {
        return try {
            val response = repository.postLike(postLikeRequest)
            NetworkResponse.Success(response)
        } catch (e: Exception) {
            NetworkResponse.Error(e.message ?: "Bir hata oluştu")
        }
    }
}