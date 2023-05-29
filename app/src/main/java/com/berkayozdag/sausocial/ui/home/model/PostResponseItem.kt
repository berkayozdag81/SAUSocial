package com.berkayozdag.sausocial.ui.home.model

data class PostResponseItem(
    val appUser: AppUser,
    val comments: List<Comment>,
    val content: String,
    val id: Int,
    val likeCount: Int,
    val publishedDate: String,
    val title: String
)