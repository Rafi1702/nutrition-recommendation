package com.example.nutritiontracker.ui.consumelog.model

import com.example.nutritiontracker.domain.model.ConsumeLog
import com.example.nutritiontracker.domain.model.GroupedConsumeLog
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

data class ConsumeLogUiState @OptIn(ExperimentalUuidApi::class) constructor(
    val id: String = Uuid.random().toString(),
    val groupedConsumeLog: GroupedConsumeLog,
    val isLoading: Boolean = false,
)
