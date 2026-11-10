package com.example.nutritiontracker.ui.food.model

import com.example.nutritiontracker.domain.model.Food

data class FoodUiState(
    val isLoading: Boolean,
    val food: Food,
)
