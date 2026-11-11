package com.example.nutritiontracker.data.remote

import com.example.nutritiontracker.data.model.consumelog.ConsumeLogRequestDto
import com.example.nutritiontracker.data.model.consumelog.ConsumeMealLogResponseDto
import com.example.nutritiontracker.data.utils.PaginatedListResponse
import com.example.nutritiontracker.ui.utils.network.AuthorizationPolicy
import com.example.nutritiontracker.ui.utils.network.ContentType
import com.example.nutritiontracker.ui.utils.network.CustomHeader
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.QueryMap


interface ConsumeLogRemoteDataSource {
    @GET("/consume/filter")
    @CustomHeader(authPolicy = [AuthorizationPolicy.BEARER], contentType = ContentType.JSON)
    suspend fun getConsumeLog(@QueryMap(encoded = true) query: Map<String, @JvmSuppressWildcards Any>): Response<PaginatedListResponse<ConsumeMealLogResponseDto>>

    @POST("/consume")
    @CustomHeader(authPolicy = [AuthorizationPolicy.BEARER], contentType = ContentType.JSON)
    suspend fun addFoodsToConsumeLog(@Body consumeLogRequestDto: ConsumeLogRequestDto): Response<Unit>

    @GET("/consume/me")
    @CustomHeader(authPolicy = [AuthorizationPolicy.BEARER], contentType = ContentType.JSON)
    suspend fun getPersonalConsumeLogs(@QueryMap(encoded = true) query: Map<String, @JvmSuppressWildcards Any>): Response<PaginatedListResponse<ConsumeMealLogResponseDto>>

}
