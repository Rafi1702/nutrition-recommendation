package com.example.nutritiontracker.domain.usecase.consumelog

import com.example.nutritiontracker.domain.AppException

sealed class ConsumeLogException(
    override val message: String?,
    val logId: String? = null
) : AppException.DomainException(message = message) {

    data class AddFoodToConsumeLogsException(override val message: String? = "Select one or more MealTypes") :
        ConsumeLogException(message = message)
}