package com.example.nutritiontracker.data.remote

import com.example.nutritiontracker.data.model.SignInRequestDto
import com.example.nutritiontracker.data.model.SignUpRequestDto
import retrofit2.http.Body
import retrofit2.http.POST


interface AuthService{
    @POST("/sign_in")
    suspend fun signIn(@Body signInRequest: SignInRequestDto)

    @POST(value = "/sign_up")
    suspend fun signUp(@Body signInRequest: SignUpRequestDto)
}