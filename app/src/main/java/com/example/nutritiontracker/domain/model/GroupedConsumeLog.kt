package com.example.nutritiontracker.domain.model

data class GroupedConsumeLog(
    val consumedAt: String,
    val consumes: List<ConsumeLog>
)
