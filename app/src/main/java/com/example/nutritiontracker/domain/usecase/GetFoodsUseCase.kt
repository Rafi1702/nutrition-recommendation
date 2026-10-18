package com.example.nutritiontracker.domain.usecase

import com.example.nutritiontracker.domain.model.Food
import com.example.nutritiontracker.domain.repository.FoodRepository
import javax.inject.Inject

class GetFoodsUseCase @Inject constructor(private val foodRepository: FoodRepository) {
    suspend operator fun invoke(): Result<List<Food>>{
        return foodRepository.getFoods()
    }
}