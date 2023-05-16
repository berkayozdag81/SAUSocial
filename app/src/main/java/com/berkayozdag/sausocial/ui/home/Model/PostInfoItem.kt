package com.berkayozdag.sausocial.ui.home.Model

data class PostInfoItem(
    val id: Int,
    val title: String,
    val content: String,
    val publishedDate: String,
    val comments: Int
)