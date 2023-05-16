package com.berkayozdag.sausocial.ui.authentication.model

data class LoginResponse(
   val id: Int,
   val token: String,
   val expireDate: String,
)