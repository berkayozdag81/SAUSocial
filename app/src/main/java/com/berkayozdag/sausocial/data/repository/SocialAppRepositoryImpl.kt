package com.berkayozdag.sausocial.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.berkayozdag.sausocial.data.api.SocialAppApi
import com.berkayozdag.sausocial.data.entities.CommentRequest
import com.berkayozdag.sausocial.data.entities.CommentResponse
import com.berkayozdag.sausocial.data.entities.FollowRequest
import com.berkayozdag.sausocial.data.entities.Post
import com.berkayozdag.sausocial.data.entities.PostLikeRequest
import com.berkayozdag.sausocial.data.entities.PostRequest
import com.berkayozdag.sausocial.data.entities.PostResponse
import com.berkayozdag.sausocial.data.entities.ProfileResponse
import com.berkayozdag.sausocial.domain.repository.SocialAppRepository
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

class SocialAppRepositoryImpl @Inject constructor(private val api: SocialAppApi) :
    SocialAppRepository {

    @RequiresApi(Build.VERSION_CODES.N)
    private val dateFormat =
        SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault(Locale.Category.FORMAT))

    @RequiresApi(Build.VERSION_CODES.N)
    override suspend fun getPosts(): List<Post> {
        return api.getPosts().sortedByDescending { dateFormat.parse(it.publishedDate) }
    }

    override suspend fun getPostById(id: Int): Post {
        return api.getPostById(id)
    }

    override suspend fun sendPost(postRequest: PostRequest): PostResponse {
        return api.sendPost(postRequest)
    }

    override suspend fun sendComment(commentRequest: CommentRequest): CommentResponse {
        return api.sendComment(commentRequest)
    }

    override suspend fun getUserById(id: Int): ProfileResponse {
        return api.getUserById(id)
    }

    override suspend fun getUsers(): List<ProfileResponse> {
        return api.getUsers()
    }

    override suspend fun follow(followRequest: FollowRequest) {
        return api.follow(followRequest)
    }

    override suspend fun unFollow(followerId: Int, userId: Int) {
        return api.unFollow(followerId, userId)
    }

    override suspend fun postLike(postLikeRequest: PostLikeRequest) {
        return api.postLike(postLikeRequest)
    }

    override suspend fun postDisLike(appUserId: Int, postId: Int) {
        return api.postDisLike(appUserId, postId)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override suspend fun getFollowingPosts(appUserId: Int): List<Post> {
        return api.getFollowingPosts(appUserId)
    }

    override suspend fun postDelete(id: Int) {
        return api.postDelete(id)
    }

}