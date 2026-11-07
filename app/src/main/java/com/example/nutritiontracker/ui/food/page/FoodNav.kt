package com.example.nutritiontracker.ui.food.page


import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.nutritiontracker.ui.food.viewmodel.FoodRecommendationViewModel

object FoodPath {
    const val BASE_PATH = "/food"
    const val FOOD_RECOMMENDATIONS = "$BASE_PATH/recommendation"
    const val FOOD_LIST = "$BASE_PATH/list"

}

fun NavGraphBuilder.foodGraph(modifier: Modifier = Modifier) {
    composable(route = FoodPath.FOOD_RECOMMENDATIONS) {
        val foodRecommendationViewModel: FoodRecommendationViewModel = hiltViewModel()
        FoodRecommendationsPage(modifier = modifier, foodRecommendationViewModel)
    }

    composable(route = FoodPath.FOOD_LIST){
        FoodListPage(modifier = Modifier)
    }
}
