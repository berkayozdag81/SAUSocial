package com.berkayozdag.sausocial.model

data class Post(
    val appUser: AppUser,
    val comments: List<Comment>,
    val content: String,
    val id: Int,
    val likes: List<Like>,
    val publishedDate: String,
) {
    fun isUserLikedThisPost(appUserId: Int): Boolean {
        return likes.any { it.appUserId == appUserId }
    }
}