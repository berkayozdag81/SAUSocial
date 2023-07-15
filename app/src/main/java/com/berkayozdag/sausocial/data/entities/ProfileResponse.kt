package com.berkayozdag.sausocial.data.entities

data class ProfileResponse(
    val email: String,
    val followers: List<Follower>,
    val followings: List<Following>,
    val id: Int,
    val name: String,
    val part: String?,
    val posts: List<Post>,
    val profileImageUrl: String,
    val isGroup: Boolean,
    val surname: String
)