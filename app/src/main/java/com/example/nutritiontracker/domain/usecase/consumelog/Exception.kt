package com.example.nutritiontracker.domain.usecase.consumelog

import com.example.nutritiontracker.domain.AppException

data class ConsumeLogException(
    override val message: String,
    val logId: String? = null
) : AppException.DomainException(message = "")