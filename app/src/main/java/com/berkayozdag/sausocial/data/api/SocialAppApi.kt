package com.berkayozdag.sausocial.data.api

import com.berkayozdag.sausocial.data.entities.CommentRequest
import com.berkayozdag.sausocial.data.entities.CommentResponse
import com.berkayozdag.sausocial.data.entities.FollowRequest
import com.berkayozdag.sausocial.data.entities.LoginRequest
import com.berkayozdag.sausocial.data.entities.LoginResponse
import com.berkayozdag.sausocial.data.entities.Post
import com.berkayozdag.sausocial.data.entities.PostLikeRequest
import com.berkayozdag.sausocial.data.entities.PostRequest
import com.berkayozdag.sausocial.data.entities.PostResponse
import com.berkayozdag.sausocial.data.entities.ProfileResponse
import retrofit2.http.*

interface SocialAppApi {

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

    @GET("Posts/followerPost/{appUserId}")
    suspend fun getFollowingPosts(
        @Path("appUserId") appUserId: Int
    ): List<Post>

    @DELETE("Posts/{id}")
    suspend fun postDelete(
        @Path("id") id: Int,
    )

}