package com.example.nutritiontracker.data.model.consumelog

import com.example.nutritiontracker.data.model.food.toFood
import com.example.nutritiontracker.domain.model.ConsumeLog
import com.example.nutritiontracker.domain.model.ConsumeLogDetail
import com.example.nutritiontracker.domain.model.Food
import com.example.nutritiontracker.domain.model.GroupedConsumeLog
import com.example.nutritiontracker.domain.model.MealType

fun String.toMealType(): MealType {
    return when (this.lowercase()) {
        "breakfast" -> MealType.BREAKFAST
        "lunch" -> MealType.LUNCH
        "dinner" -> MealType.DINNER
        else -> MealType.UNSPECIFIED
    }
}

fun MealType.toLabel(): String {
    return when (this) {
        MealType.BREAKFAST -> "breakfast"
        MealType.LUNCH -> "lunch"
        MealType.DINNER -> "dinner"
        else -> ""
    }
}

fun ConsumeLogResponseDto.toConsumeLog(mealType: MealType = MealType.UNSPECIFIED): ConsumeLog =
    ConsumeLog(
        consumeLogDetail = ConsumeLogDetail(
            id = this.id,
            food = this.food.toFood(),
            eatTime = this.consumedAt,
        ),
        mealType = mealType
    )

fun ConsumeMealLogResponseDto.toConsumeLog(): ConsumeLog {
    val parsedMealType = this.mealType.toMealType()
    val firstConsume = this.consumes.firstOrNull()
    return ConsumeLog(
        consumeLogDetail = ConsumeLogDetail(
            id = firstConsume?.id ?: 0,
            food = firstConsume?.food?.toFood() ?: Food(0, "", emptyList()),
            eatTime = firstConsume?.consumedAt ?: "",
        ),
        mealType = parsedMealType
    )
}

fun GroupedConsumeLogItem.toConsumeLog(consumedAt: String): ConsumeLog =
    ConsumeLog(
        consumeLogDetail = ConsumeLogDetail(
            id = this.id,
            food = this.food.toFood(),
            eatTime = consumedAt,
        ),
        mealType = this.mealType.toMealType()
    )

fun GroupedConsumeLogResponseDto.toConsumeLogList(): List<ConsumeLog> =
    this.consumes.map { it.toConsumeLog(this.consumedAt) }

fun GroupedConsumeLogResponseDto.toGroupedConsumeLog(): GroupedConsumeLog =
    GroupedConsumeLog(
        consumedAt = this.consumedAt,
        consumes = this.toConsumeLogList()
    )

fun GroupedConsumeLogResponseDto.toConsumeLog(): ConsumeLog {
    val firstConsume = this.consumes.firstOrNull()
    return ConsumeLog(
        consumeLogDetail = ConsumeLogDetail(
            id = firstConsume?.id ?: 0,
            food = firstConsume?.food?.toFood() ?: Food(0, "", emptyList()),
            eatTime = this.consumedAt,
        ),
        mealType = firstConsume?.mealType?.toMealType() ?: MealType.UNSPECIFIED
    )
}

fun ConsumeMealLogResponseDto.toConsumeLogList(): List<ConsumeLog> {
    val parsedMealType = this.mealType.toMealType()
    return this.consumes.map { dto ->
        dto.toConsumeLog(parsedMealType)
    }
}

fun List<ConsumeLogResponseDto>.toConsumeLogList(mealType: MealType = MealType.UNSPECIFIED): List<ConsumeLog> =
    this.map { response -> response.toConsumeLog(mealType) }
