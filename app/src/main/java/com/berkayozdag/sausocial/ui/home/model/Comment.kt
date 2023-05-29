package com.berkayozdag.sausocial.ui.home.model

data class Comment(
    val appUser: AppUserComment,
    val id: Int,
    val message: String,
    val publishedDate: String
)