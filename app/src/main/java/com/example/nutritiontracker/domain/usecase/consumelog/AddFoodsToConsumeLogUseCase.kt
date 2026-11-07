package com.example.nutritiontracker.domain.usecase.consumelog

import com.example.nutritiontracker.domain.repository.ConsumeLogRepository
import javax.inject.Inject

class AddFoodsToConsumeLogUseCase @Inject constructor(private val  consumeLogRepository: ConsumeLogRepository) {

    operator suspend fun invoke(){

    }
}