package com.example.nutritiontracker.domain.repository

import com.example.nutritiontracker.domain.model.Allergens

interface AllergensRepository {
    suspend fun getAllergens(): Result<List<Allergens>>
}