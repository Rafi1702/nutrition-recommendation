package com.example.nutritiontracker.ui.food.page

import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

object FoodPath {
    const val FOOD_RECOMMENDATIONS = "/food_recommendation"
    const val FOOD_LIST = "/food_list"
}

fun NavGraphBuilder.foodGraph(modifier: Modifier = Modifier) {
    composable(route = FoodPath.FOOD_RECOMMENDATIONS) {
        FoodRecommendationsPage(modifier = modifier)
    }
}
