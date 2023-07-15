package com.berkayozdag.sausocial.domain.usecase

import com.berkayozdag.sausocial.common.util.NetworkResponse
import com.berkayozdag.sausocial.data.entities.PostRequest
import com.berkayozdag.sausocial.data.entities.PostResponse
import com.berkayozdag.sausocial.domain.repository.SocialAppRepository

class SendPost(private val repository: SocialAppRepository) {
    suspend operator fun invoke(postRequest: PostRequest): NetworkResponse<PostResponse> {
        return try {
            val response = repository.sendPost(postRequest)
            NetworkResponse.Success(response)
        } catch (e: Exception) {
            NetworkResponse.Error(e.message ?: "Bir hata oluştu")
        }
    }
}