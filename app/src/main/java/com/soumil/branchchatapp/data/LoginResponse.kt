package com.soumil.branchchatapp.data

data class LoginRequest(
    val username: String,
    val password: String
)

data class LoginResponse(
    val auth_token: String
)