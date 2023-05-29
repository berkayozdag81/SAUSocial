package com.berkayozdag.sausocial.ui.post_detail.model

data class Comment(
    val appUser: AppUserX,
    val id: Int,
    val message: String,
    val publishedDate: String
)