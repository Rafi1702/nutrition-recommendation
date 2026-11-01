package com.example.nutritiontracker.data.model.allergens


data class AllergensListItemsDto(
    val items: List<AllergensResponseDto>
)

data class AllergensResponseDto(
    val id: Int,
    val name: String,
)
