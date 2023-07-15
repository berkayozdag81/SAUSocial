package com.berkayozdag.sausocial.data.entities

data class CommentRequest(
    val message: String,
    val publishedDate: String,
    val postId: Int,
    val appUserId: Int
)
