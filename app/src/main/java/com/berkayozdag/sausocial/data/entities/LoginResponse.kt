package com.berkayozdag.sausocial.data.entities

data class LoginResponse(
    val id: Int,
    val token: String,
    val expireDate: String,
    val isGroup: Boolean,
    val name: String,
    val surname: String,
    val imageUrl: String,
)