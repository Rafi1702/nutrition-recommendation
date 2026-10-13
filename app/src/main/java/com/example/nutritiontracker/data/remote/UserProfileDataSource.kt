package com.example.nutritiontracker.data.remote

import com.example.nutritiontracker.data.model.user.UserProfileResponseDto
import com.example.nutritiontracker.ui.utils.network.AuthorizationPolicy
import com.example.nutritiontracker.ui.utils.network.ContentType
import com.example.nutritiontracker.ui.utils.network.CustomHeader
import retrofit2.Response
import retrofit2.http.GET

interface UserProfileDataSource {
    @GET("/user/profile")
    @CustomHeader(authPolicy = [AuthorizationPolicy.BEARER], contentType = ContentType.JSON)
    suspend fun getUserProfile(): Response<UserProfileResponseDto?>
}