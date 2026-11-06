package com.example.nutritiontracker.data.remote

import com.example.nutritiontracker.data.model.allergens.AllergensListItemsDto
import com.example.nutritiontracker.ui.utils.network.AuthorizationPolicy
import com.example.nutritiontracker.ui.utils.network.ContentType
import com.example.nutritiontracker.ui.utils.network.CustomHeader
import retrofit2.Response
import com.example.nutritiontracker.data.utils.Response as UtilResponse
import retrofit2.http.GET

interface AllergensRemoteDataSource {
    @GET("/allergens/all")
    @CustomHeader(authPolicy = [AuthorizationPolicy.BEARER], contentType = ContentType.JSON)
    suspend fun getAllergens(): Response<UtilResponse<AllergensListItemsDto>>
}