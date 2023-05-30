package com.berkayozdag.sausocial.model

data class AppUser(
        val email: String,
        val id: Int,
        val name: String,
        val part: String,
        val profileImagUrl: Any,
        val surname: String
)