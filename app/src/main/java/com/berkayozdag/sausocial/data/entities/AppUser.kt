package com.berkayozdag.sausocial.data.entities

data class AppUser(
    val email: String,
    val id: Int,
    val name: String,
    val part: String,
    val profileImageUrl: String,
    val surname: String,
    val followers: List<Follower>
)