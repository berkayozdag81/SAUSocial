package com.berkayozdag.sausocial.model

data class Comment(
        val appUser: AppUser,
        val id: Int,
        val message: String,
        val publishedDate: String
)