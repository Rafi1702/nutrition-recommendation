package com.example.nutritiontracker.data.model.allergens

import com.example.nutritiontracker.domain.model.Allergens

fun AllergensResponseDto.toAllergens(): Allergens = Allergens(
    id = this.id,
    name = this.name,
)