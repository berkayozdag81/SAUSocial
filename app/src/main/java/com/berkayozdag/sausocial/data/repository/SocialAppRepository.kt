package com.berkayozdag.sausocial.data.repository

import com.berkayozdag.sausocial.data.NetworkResponse
import com.berkayozdag.sausocial.data.api.ApiService
import com.berkayozdag.sausocial.model.Post
import com.berkayozdag.sausocial.model.profile.ProfileResponse
import javax.inject.Inject

class SocialAppRepository @Inject constructor(
    private val api: ApiService,
) {

    suspend fun getPosts(): NetworkResponse<List<Post>> {
        return try {
            val response = api.getPosts()
            NetworkResponse.Success(response)
        } catch (e: Exception) {
            NetworkResponse.Error(e.message ?: "Bir hata oluştu")
        }
    }

    suspend fun getPostById(id: Int): NetworkResponse<Post> {
        return try {
            val response = api.getPostById(id)
            NetworkResponse.Success(response)
        } catch (e: Exception) {
            NetworkResponse.Error(e.message ?: "Bir hata oluştu")
        }
    }

    suspend fun getUserById(id: Int): NetworkResponse<ProfileResponse> {
        return try {
            val response = api.getUserById(id)
            NetworkResponse.Success(response)
        } catch (e: Exception) {
            NetworkResponse.Error(e.message ?: "Bir hata oluştu")
        }
    }
}