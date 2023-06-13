package com.berkayozdag.sausocial.model.profile

import com.berkayozdag.sausocial.model.Follower
import com.berkayozdag.sausocial.model.Following
import com.berkayozdag.sausocial.model.Post

data class ProfileResponse(
    val email: String,
    val followers: List<Follower>,
    val followings: List<Following>,
    val id: Int,
    val name: String,
    val part: String,
    val posts: List<Post>,
    val profileImageUrl: String,
    val isGroup: Boolean,
    val surname: String
)