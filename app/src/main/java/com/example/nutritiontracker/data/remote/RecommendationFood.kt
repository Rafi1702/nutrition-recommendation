package com.example.nutritiontracker.data.remote

import com.example.nutritiontracker.data.model.recommendation_foods.RecommendationFoodResponseDto
import com.example.nutritiontracker.data.utils.Response
import com.example.nutritiontracker.ui.utils.network.AuthorizationPolicy
import com.example.nutritiontracker.ui.utils.network.ContentType
import com.example.nutritiontracker.ui.utils.network.CustomHeader
import retrofit2.Response as RetrofitResponse
import retrofit2.http.GET

interface RecommendationFoodRemoteDataSource {
    @GET("/recommendations/foods")
    @CustomHeader(authPolicy = [AuthorizationPolicy.BEARER], contentType = ContentType.JSON)
    suspend fun getRecommendedFoods(): RetrofitResponse<Response<List<RecommendationFoodResponseDto>>>
}
