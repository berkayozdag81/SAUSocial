package com.berkayozdag.sausocial.data.repository

import com.berkayozdag.sausocial.data.NetworkResponse
import com.berkayozdag.sausocial.data.api.ApiService
import com.berkayozdag.sausocial.model.*
import com.berkayozdag.sausocial.model.profile.ProfileResponse
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class SocialAppRepository @Inject constructor(
    private val api: ApiService,
) {

    private val dateFormat = SimpleDateFormat("dd.MM.yyyy hh:mm:ss", Locale.getDefault())

    suspend fun getPosts(): NetworkResponse<List<Post>> {
        return try {
            val response = api.getPosts().sortedByDescending { dateFormat.parse(it.publishedDate) }
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

    suspend fun sendPost(postRequest: PostRequest): NetworkResponse<PostResponse> {
        return try {
            val response = api.sendPost(postRequest)
            NetworkResponse.Success(response)
        } catch (e: Exception) {
            NetworkResponse.Error(e.message ?: "Bir hata oluştu")
        }
    }

    suspend fun sendComment(commentRequest: CommentRequest): NetworkResponse<CommentResponse> {
        return try {
            val response = api.sendComment(commentRequest)
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

    suspend fun getUsers(): NetworkResponse<List<ProfileResponse>> {
        return try {
            val response = api.getUsers()
            NetworkResponse.Success(response)
        } catch (e: Exception) {
            NetworkResponse.Error(e.message ?: "Bir hata oluştu")
        }
    }

    suspend fun follow(followRequest: FollowRequest): NetworkResponse<Any> {
        return try {
            val response = api.follow(followRequest)
            NetworkResponse.Success(response)
        } catch (e: Exception) {
            NetworkResponse.Error(e.message ?: "Bir hata oluştu")
        }
    }

    suspend fun unFollow(followerId: Int, userId: Int): NetworkResponse<Any> {
        return try {
            val response = api.unFollow(followerId, userId)
            NetworkResponse.Success(response)
        } catch (e: Exception) {
            NetworkResponse.Error(e.message ?: "Bir hata oluştu")
        }
    }

    suspend fun postLike(postLikeRequest: PostLikeRequest): NetworkResponse<Any> {
        return try {
            val response = api.postLike(postLikeRequest)
            NetworkResponse.Success(response)
        } catch (e: Exception) {
            NetworkResponse.Error(e.message ?: "Bir hata oluştu")
        }
    }

    suspend fun postDislike(appUserId: Int, postId: Int): NetworkResponse<Any> {
        return try {
            val response = api.postDisLike(appUserId, postId)
            NetworkResponse.Success(response)
        } catch (e: Exception) {
            NetworkResponse.Error(e.message ?: "Bir hata oluştu")
        }
    }
}