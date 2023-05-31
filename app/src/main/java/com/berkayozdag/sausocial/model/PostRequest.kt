package com.berkayozdag.sausocial.model

data class PostRequest(
    val content: String,
    val publishedDate: String,
    val likeCount: Int,
    val appUserId: Int
)
