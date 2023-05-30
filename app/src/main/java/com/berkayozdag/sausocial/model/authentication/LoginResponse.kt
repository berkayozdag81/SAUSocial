package com.berkayozdag.sausocial.model.authentication

data class LoginResponse(
   val id: Int,
   val token: String,
   val expireDate: String,
)