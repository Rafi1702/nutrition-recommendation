package com.example.nutritiontracker.domain.usecase

import com.example.nutritiontracker.domain.model.Allergens
import com.example.nutritiontracker.domain.repository.AllergensRepository
import javax.inject.Inject

class GetAllergensUseCase @Inject constructor(private val allergensRepository: AllergensRepository) {
    suspend operator fun invoke(): Result<List<Allergens>>{
        return allergensRepository.getAllergens()
    }
}