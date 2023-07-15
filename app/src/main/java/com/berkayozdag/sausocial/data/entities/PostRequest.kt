package com.berkayozdag.sausocial.data.entities

data class PostRequest(
    val content: String,
    val publishedDate: String,
    val likeCount: Int,
    val appUserId: Int
)
