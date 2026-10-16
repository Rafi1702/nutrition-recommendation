package com.example.nutritiontracker.data.repository

import com.example.nutritiontracker.data.model.food.toFoods
import com.example.nutritiontracker.data.model.user.toUserProfile
import com.example.nutritiontracker.data.remote.FoodRemoteDataSource
import com.example.nutritiontracker.data.safeCall
import com.example.nutritiontracker.domain.model.Food
import com.example.nutritiontracker.domain.repository.FoodRepository
import javax.inject.Inject

class FoodRepositoryImpl @Inject constructor(
    private val foodRemoteDataSource: FoodRemoteDataSource
) :
    FoodRepository {
    override suspend fun getFoods(): Result<List<Food>> {
        val foodsResponse = safeCall {
            foodRemoteDataSource.getFoods(mapOf("" to ""))
        }
        return foodsResponse.fold(
            onSuccess = { foods ->
                Result.success(foods.items.toFoods())
            },
            onFailure = { error ->
                Result.failure(error)
            }
        )
    }
}