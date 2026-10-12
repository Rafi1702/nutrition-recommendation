package com.example.nutritiontracker.data.remote

import com.example.nutritiontracker.data.model.AuthResponseDto
import com.example.nutritiontracker.data.model.SignInRequestDto
import com.example.nutritiontracker.data.model.SignUpRequestDto
import com.example.nutritiontracker.ui.utils.network.ContentType
import com.example.nutritiontracker.ui.utils.network.CustomHeader
import retrofit2.http.Body
import retrofit2.http.POST


interface AuthService{
    @POST("/auth/sign_in")
    @CustomHeader(contentType = ContentType.JSON)
    suspend fun signIn(@Body signInRequest: SignInRequestDto): AuthResponseDto

    @POST(value = "/auth/sign_up")
    @CustomHeader(contentType = ContentType.JSON)
    suspend fun signUp(@Body signInRequest: SignUpRequestDto)
}