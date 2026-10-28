package com.example.nutritiontracker.data.remote

import com.example.nutritiontracker.data.model.auth.AuthResponseDto
import com.example.nutritiontracker.data.model.auth.RefreshTokenRequestDto
import com.example.nutritiontracker.data.model.auth.SignInRequestDto
import com.example.nutritiontracker.data.model.auth.SignUpRequestDto
import com.example.nutritiontracker.ui.utils.network.ContentType
import com.example.nutritiontracker.ui.utils.network.CustomHeader
import retrofit2.http.Body
import retrofit2.Response
import retrofit2.http.POST
import com.example.nutritiontracker.data.utils.Response as UtilResponse


interface AuthService{
    @POST("/auth/sign_in")
    @CustomHeader(contentType = ContentType.JSON)
    suspend fun signIn(@Body signInRequest: SignInRequestDto): Response<UtilResponse<AuthResponseDto>>

    @POST(value = "/auth/sign_up")
    @CustomHeader(contentType = ContentType.JSON)
    suspend fun signUp(@Body signInRequest: SignUpRequestDto)


    @POST(value = "/auth/refresh_token")
    suspend fun refreshToken(@Body refreshToken: RefreshTokenRequestDto): Response<UtilResponse<AuthResponseDto>>
}
