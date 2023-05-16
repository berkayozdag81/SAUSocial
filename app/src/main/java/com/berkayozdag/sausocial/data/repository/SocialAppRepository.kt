package com.berkayozdag.sausocial.data.repository

import com.berkayozdag.sausocial.data.NetworkResponse
import com.berkayozdag.sausocial.data.api.ApiService
import com.berkayozdag.sausocial.ui.home.Model.PostInfoItem
import javax.inject.Inject

class SocialAppRepository @Inject constructor(
    private val api: ApiService,
) {

    suspend fun getPosts(): NetworkResponse<List<PostInfoItem>> {
        return try {
            val response = api.getPosts()
            NetworkResponse.Success(response)
        } catch (e: Exception) {
            NetworkResponse.Error(e.message ?: "Bir hata oluştu")
        }
    }
}