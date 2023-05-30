package com.berkayozdag.sausocial.model

data class Post(
    val appUser: AppUser,
    val appUserId: Int,
    val comments: List<Comment>,
    val content: String,
    val id: Int,
    val likeCount: Int,
    val publishedDate: String,
    val title: String
)