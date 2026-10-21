package com.example.nutritiontracker.ui.food.page

import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation

object FoodPath {
    const val FOOD_RECOMMENDATIONS = "/recommendation"
    const val FOOD_LIST = "/list"
    const val BASE_PATH = "/food"
}

fun NavGraphBuilder.foodGraph(modifier: Modifier = Modifier) {
    composable(route = FoodPath.FOOD_RECOMMENDATIONS) {
        FoodRecommendationsPage(modifier = modifier)
    }

    composable(route = FoodPath.FOOD_LIST){
        FoodListPage(modifier = Modifier)
    }
}
