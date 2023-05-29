package com.berkayozdag.sausocial.data.api

import com.berkayozdag.sausocial.ui.authentication.model.LoginRequest
import com.berkayozdag.sausocial.ui.authentication.model.LoginResponse
import com.berkayozdag.sausocial.ui.home.model.PostResponseItem
import com.berkayozdag.sausocial.ui.post_detail.model.PostDetailResponseItem
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    @POST("Auth/Login")
    suspend fun login(
        @Body request: LoginRequest
    ): LoginResponse


    @GET("Posts")
    suspend fun getPosts(): List<PostResponseItem>

    @GET("Posts/{id}")
    suspend fun getPostById(@Path("id")id:Int): List<PostDetailResponseItem>
}