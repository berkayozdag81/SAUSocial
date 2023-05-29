package com.berkayozdag.sausocial.data.api

import com.berkayozdag.sausocial.ui.authentication.model.LoginRequest
import com.berkayozdag.sausocial.ui.authentication.model.LoginResponse
import com.berkayozdag.sausocial.ui.home.model.PostResponseItem
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    @POST("Auth/Login")
    suspend fun login(
        @Body request: LoginRequest
    ): LoginResponse


    @GET("Posts")
    suspend fun getPosts(): List<PostResponseItem>
}