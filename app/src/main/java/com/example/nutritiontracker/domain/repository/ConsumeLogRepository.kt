package com.example.nutritiontracker.domain.repository

import androidx.paging.PagingData
import com.example.nutritiontracker.domain.model.ConsumeLog
import kotlinx.coroutines.flow.Flow

interface ConsumeLogRepository {
    fun getConsumeLog(): Flow<PagingData<ConsumeLog>>
}