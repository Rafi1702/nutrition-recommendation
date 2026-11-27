package com.example.nutritiontracker.domain.usecase.consumelog

import androidx.paging.PagingData
import com.example.nutritiontracker.domain.model.GroupedConsumeLog
import com.example.nutritiontracker.domain.model.MealType
import com.example.nutritiontracker.domain.repository.ConsumeLogRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPersonalGroupedConsumeLogUseCase @Inject constructor(private val consumeLogRepository: ConsumeLogRepository) {
    operator fun invoke(
        mealType: MealType,
        startDate: String? = null,
        endDate: String? = null,
        consumedAt: String? = null,
        groupBy: String? = null,
    ): Flow<PagingData<GroupedConsumeLog>> {
        return consumeLogRepository.getGroupedPersonalConsumeLog(
            startDate = startDate,
            endDate = endDate,
            groupBy = groupBy,
            mealType = mealType
        )
    }
}