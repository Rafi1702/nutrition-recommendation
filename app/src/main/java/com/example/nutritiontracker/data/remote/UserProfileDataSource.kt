package com.example.nutritiontracker.data.remote

import com.example.nutritiontracker.data.model.user.UserProfileResponseDto
import com.example.nutritiontracker.data.model.user.UserProfileUpdateCreateRequestDto
import com.example.nutritiontracker.ui.utils.network.AuthorizationPolicy
import com.example.nutritiontracker.ui.utils.network.ContentType
import com.example.nutritiontracker.ui.utils.network.CustomHeader
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import com.example.nutritiontracker.data.utils.Response as UtilResponse


interface UserProfileDataSource {
    @GET("/user/profile")
    @CustomHeader(authPolicy = [AuthorizationPolicy.BEARER], contentType = ContentType.JSON)
    suspend fun getUserProfile(): Response<UtilResponse<UserProfileResponseDto?>>


    @POST("/user/profile")
    @CustomHeader(authPolicy = [AuthorizationPolicy.BEARER], contentType = ContentType.JSON)
    suspend fun updateUserProfile(): Response<Unit>

    @GET("/user/profile/daily-total-consumption")
    @CustomHeader(authPolicy = [AuthorizationPolicy.BEARER], contentType = ContentType.JSON)
    suspend fun getUserDailyConsumption(): Response<UtilResponse<UserProfileResponseDto?>>

    @POST("/user/profile")
    @CustomHeader(authPolicy = [AuthorizationPolicy.BEARER], contentType = ContentType.JSON)
    suspend fun createUserProfile(@Body userProfile: UserProfileUpdateCreateRequestDto): Response<UtilResponse<UserProfileResponseDto?>>
}