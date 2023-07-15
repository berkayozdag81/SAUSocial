package com.berkayozdag.sausocial.domain.usecase

import com.berkayozdag.sausocial.common.util.NetworkResponse
import com.berkayozdag.sausocial.data.entities.Post
import com.berkayozdag.sausocial.domain.repository.SocialAppRepository

class GetPosts(private val repository: SocialAppRepository) {
    suspend operator fun invoke(): NetworkResponse<List<Post>> {
        return try {
            val response = repository.getPosts()
            NetworkResponse.Success(response)
        } catch (e: Exception) {
            NetworkResponse.Error(e.message ?: "Bir hata oluştu")
        }
    }
}