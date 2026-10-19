package com.example.nutritiontracker.domain.usecase

import androidx.paging.PagingData
import com.example.nutritiontracker.data.utils.defaultPageConfig
import com.example.nutritiontracker.domain.model.Food
import com.example.nutritiontracker.domain.repository.FoodRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFoodsUseCase @Inject constructor(private val foodRepository: FoodRepository) {
    operator fun invoke(): Flow<PagingData<Food>> {
        return foodRepository.getFoods(pagingConfig = defaultPageConfig())
    }
}