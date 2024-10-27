package com.soumil.branchchatapp.api

data class LoginRequest(
    val username: String,
    val password: String
)

data class LoginResponse(
    val auth_token: String
)