package com.berkayozdag.sausocial.data.entities

data class Comment(
    val appUser: AppUser,
    val id: Int,
    val message: String,
    val publishedDate: String
)