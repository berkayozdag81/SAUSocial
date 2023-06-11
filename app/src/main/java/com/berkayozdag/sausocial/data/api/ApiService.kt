package com.berkayozdag.sausocial.data.api

import com.berkayozdag.sausocial.model.*
import com.berkayozdag.sausocial.model.authentication.LoginRequest
import com.berkayozdag.sausocial.model.authentication.LoginResponse
import com.berkayozdag.sausocial.model.profile.ProfileResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    @POST("Auth/Login")
    suspend fun login(
        @Body request: LoginRequest
    ): LoginResponse

    @GET("Posts")
    suspend fun getPosts(): List<Post>

    @GET("Posts/{id}")
    suspend fun getPostById(@Path("id") id: Int): Post

    @POST("Posts")
    suspend fun sendPost(
        @Body postRequest: PostRequest
    ): PostResponse

    @POST("Comment")
    suspend fun sendComment(
        @Body commentRequest: CommentRequest
    ): CommentResponse

    @GET("User/{id}")
    suspend fun getUserById(@Path("id") id: Int): ProfileResponse

    @GET("User")
    suspend fun getUsers(): List<ProfileResponse>

    @POST("Follower")
    suspend fun follow(
        @Body followRequest: FollowRequest
    )

    @DELETE("Follower/{followerId}/{appUserId}")
    suspend fun unFollow(
        @Path("followerId") followerId: Int,
        @Path("appUserId") appUserId: Int
    )

    @POST("Like")
    suspend fun postLike(
        @Body postLikeRequest: PostLikeRequest
    )

    @DELETE("Like/{appUserId}/{postId}")
    suspend fun postDisLike(
        @Path("appUserId") appUserId: Int,
        @Path("postId") postId: Int
    )

}