package com.example.nutritiontracker.domain.repository

import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.nutritiontracker.data.utils.defaultPageConfig
import com.example.nutritiontracker.domain.model.Food
import kotlinx.coroutines.flow.Flow

interface FoodRepository {
    suspend fun getFoods() : Result<List<Food>>
     fun getFoods(pagingConfig: PagingConfig = defaultPageConfig()): Flow<PagingData<Food>>
}