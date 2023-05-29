package com.berkayozdag.sausocial.ui.post_detail.model

data class PostDetailResponseItem(
    val appUser: AppUser,
    val comments: List<Comment>,
    val content: String,
    val id: Int,
    val likeCount: Int,
    val publishedDate: String,
    val title: String
)