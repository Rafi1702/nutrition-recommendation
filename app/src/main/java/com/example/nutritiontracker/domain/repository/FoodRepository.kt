package com.example.nutritiontracker.domain.repository

import com.example.nutritiontracker.domain.model.Food

interface FoodRepository {
    suspend fun getFoods() : Result<List<Food>>
}