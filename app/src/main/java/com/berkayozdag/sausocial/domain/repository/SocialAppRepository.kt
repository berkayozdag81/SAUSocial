package com.berkayozdag.sausocial.domain.repository

import com.berkayozdag.sausocial.data.entities.CommentRequest
import com.berkayozdag.sausocial.data.entities.CommentResponse
import com.berkayozdag.sausocial.data.entities.FollowRequest
import com.berkayozdag.sausocial.data.entities.Post
import com.berkayozdag.sausocial.data.entities.PostLikeRequest
import com.berkayozdag.sausocial.data.entities.PostRequest
import com.berkayozdag.sausocial.data.entities.PostResponse
import com.berkayozdag.sausocial.data.entities.ProfileResponse

interface SocialAppRepository {

    suspend fun getPosts(): List<Post>

    suspend fun getPostById(id: Int): Post

    suspend fun sendPost(postRequest: PostRequest): PostResponse

    suspend fun sendComment(commentRequest: CommentRequest): CommentResponse

    suspend fun getUserById(id: Int): ProfileResponse

    suspend fun getUsers(): List<ProfileResponse>

    suspend fun follow(followRequest: FollowRequest)

    suspend fun unFollow(followerId: Int, appUserId: Int)

    suspend fun postLike(postLikeRequest: PostLikeRequest)

    suspend fun postDisLike(appUserId: Int, postId: Int)

    suspend fun getFollowingPosts(appUserId: Int): List<Post>

    suspend fun postDelete(id: Int)

}