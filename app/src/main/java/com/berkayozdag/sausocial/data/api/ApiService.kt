package com.berkayozdag.sausocial.data.api

import com.berkayozdag.sausocial.data.NetworkResponse
import com.berkayozdag.sausocial.ui.authentication.model.LoginRequest
import com.berkayozdag.sausocial.ui.authentication.model.LoginResponse
import com.berkayozdag.sausocial.ui.home.Model.PostInfoItem
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    @POST("Auth/Login")
    suspend fun login(
        @Body request: LoginRequest
    ): LoginResponse


    @GET("Posts")
    suspend fun getPosts(): List<PostInfoItem>
}