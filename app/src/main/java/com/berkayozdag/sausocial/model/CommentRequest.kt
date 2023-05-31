package com.berkayozdag.sausocial.model

data class CommentRequest(
    val message: String,
    val publishedDate: String,
    val postId: Int,
    val appUserId: Int
)
