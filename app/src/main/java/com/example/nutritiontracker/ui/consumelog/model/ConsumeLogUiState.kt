package com.example.nutritiontracker.ui.consumelog.model

import com.example.nutritiontracker.domain.model.ConsumeLog

data class ConsumeLogUiState(
    val consumeLog: ConsumeLog,
    val isLoading: Boolean = false,
)
