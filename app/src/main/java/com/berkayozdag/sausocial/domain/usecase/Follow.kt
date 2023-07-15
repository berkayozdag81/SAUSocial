package com.berkayozdag.sausocial.domain.usecase

import com.berkayozdag.sausocial.common.util.NetworkResponse
import com.berkayozdag.sausocial.data.entities.FollowRequest
import com.berkayozdag.sausocial.domain.repository.SocialAppRepository

class Follow(private val repository: SocialAppRepository) {
    suspend operator fun invoke(followRequest: FollowRequest):NetworkResponse<Any>{
        return try {
            val response = repository.follow(followRequest)
            NetworkResponse.Success(response)
        } catch (e: Exception) {
            NetworkResponse.Error(e.message ?: "Bir hata oluştu")
        }
    }
}