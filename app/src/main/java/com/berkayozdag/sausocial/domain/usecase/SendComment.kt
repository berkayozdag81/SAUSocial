package com.berkayozdag.sausocial.domain.usecase

import com.berkayozdag.sausocial.common.util.NetworkResponse
import com.berkayozdag.sausocial.data.entities.CommentRequest
import com.berkayozdag.sausocial.data.entities.CommentResponse
import com.berkayozdag.sausocial.domain.repository.SocialAppRepository

class SendComment(private val repository: SocialAppRepository) {
    suspend operator fun invoke(commentRequest: CommentRequest): NetworkResponse<CommentResponse> {
        return try {
            val response = repository.sendComment(commentRequest)
            NetworkResponse.Success(response)
        } catch (e: Exception) {
            NetworkResponse.Error(e.message ?: "Bir hata oluştu")
        }
    }
}