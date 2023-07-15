package com.berkayozdag.sausocial.domain.usecase

import com.berkayozdag.sausocial.common.util.NetworkResponse
import com.berkayozdag.sausocial.data.entities.Post
import com.berkayozdag.sausocial.domain.repository.SocialAppRepository

class GetPostById(private val repository: SocialAppRepository) {
    suspend operator fun invoke(id: Int): NetworkResponse<Post> {
        return try {
            val response = repository.getPostById(id)
            NetworkResponse.Success(response)
        } catch (e: Exception) {
            NetworkResponse.Error(e.message ?: "Bir hata oluştu")
        }
    }
}