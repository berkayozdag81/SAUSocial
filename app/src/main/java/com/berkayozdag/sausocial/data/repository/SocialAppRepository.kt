package com.berkayozdag.sausocial.data.repository

import com.berkayozdag.sausocial.data.NetworkResponse
import com.berkayozdag.sausocial.data.api.ApiService
import com.berkayozdag.sausocial.ui.home.model.PostResponseItem
import com.berkayozdag.sausocial.ui.post_detail.model.PostDetailResponseItem
import javax.inject.Inject

class SocialAppRepository @Inject constructor(
    private val api: ApiService,
) {

    suspend fun getPosts(): NetworkResponse<List<PostResponseItem>> {
        return try {
            val response = api.getPosts()
            NetworkResponse.Success(response)
        } catch (e: Exception) {
            NetworkResponse.Error(e.message ?: "Bir hata oluştu")
        }
    }

    suspend fun getPostById(id: Int): NetworkResponse<List<PostDetailResponseItem>> {
        return try {
            val response = api.getPostById(id)
            NetworkResponse.Success(response)
        } catch (e: Exception) {
            NetworkResponse.Error(e.message ?: "Bir hata oluştu")
        }
    }
}