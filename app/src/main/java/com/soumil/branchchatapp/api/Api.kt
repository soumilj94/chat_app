package com.soumil.branchchatapp.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiService {
    @Headers("Content-Type: application/json")
    @POST("api/login")
    fun login(@Body request: LoginRequest):Call<LoginResponse>

    @GET("api/messages")
    fun getMessages(
        @Header("X-Branch-Auth-Token") authToken: String
    ): Call<List<Message>>
}