package com.example.nutritiontracker.domain.usecase

import androidx.paging.PagingData
import com.example.nutritiontracker.domain.model.ConsumeLog
import com.example.nutritiontracker.domain.repository.ConsumeLogRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetConsumeLogUseCase @Inject constructor(private val  consumeLogRepository: ConsumeLogRepository)  {
    operator fun invoke(): Flow<PagingData<ConsumeLog>> {
        return consumeLogRepository.getConsumeLog()
    }
}