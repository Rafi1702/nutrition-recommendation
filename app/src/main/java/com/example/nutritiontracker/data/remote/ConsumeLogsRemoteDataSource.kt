package com.example.nutritiontracker.data.remote

import com.example.nutritiontracker.data.model.consumelog.ConsumeLogResponseDto
import com.example.nutritiontracker.data.model.food.FoodResponseDto
import com.example.nutritiontracker.data.utils.CursorPaginate
import com.example.nutritiontracker.domain.model.ConsumeLog
import com.example.nutritiontracker.domain.model.Food
import com.example.nutritiontracker.domain.model.MacroNutrients
import com.example.nutritiontracker.domain.model.MacroType
import com.example.nutritiontracker.ui.utils.network.AuthorizationPolicy
import com.example.nutritiontracker.ui.utils.network.ContentType
import com.example.nutritiontracker.ui.utils.network.CustomHeader
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap


interface ConsumeLogRemoteDataSource {
    @GET("/consume")
    @CustomHeader(authPolicy = [AuthorizationPolicy.BEARER], contentType = ContentType.JSON)
    suspend fun getConsumeLog(@QueryMap(encoded = true) query: Map<String, Int>): Response<CursorPaginate<ConsumeLogResponseDto>>
}
