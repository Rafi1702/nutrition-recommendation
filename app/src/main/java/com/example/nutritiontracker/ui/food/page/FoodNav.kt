package com.example.nutritiontracker.ui.food.page

import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.nutritiontracker.data.model.consumelog.toMealType
import com.example.nutritiontracker.domain.model.MealType
import com.example.nutritiontracker.ui.components.nowYyyyMMDD
import com.example.nutritiontracker.ui.food.viewmodel.FoodListViewModel
import com.example.nutritiontracker.ui.food.viewmodel.FoodRecommendationViewModel

object FoodPath {
    const val BASE_PATH = "/food"
    const val FOOD_RECOMMENDATIONS = "/food/recommendation"
    const val FOOD_LIST_BASE = "/food/list"
    const val FOOD_LIST_ROUTE = "/food/list/{consumedAt}/{mealType}"

    fun createFoodListRoute(
        consumedAt: String = nowYyyyMMDD(),
        mealType: String = "UNSPECIFIED"
    ): String {
        return "$FOOD_LIST_BASE/$consumedAt/$mealType"
    }
}

fun NavGraphBuilder.foodGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    composable(route = FoodPath.FOOD_RECOMMENDATIONS) {
        val foodRecommendationViewModel: FoodRecommendationViewModel = hiltViewModel()
        FoodRecommendationsPage(modifier = modifier, viewModel = foodRecommendationViewModel)
    }

    composable(
        route = FoodPath.FOOD_LIST_ROUTE,
        arguments = listOf(
            navArgument("consumedAt") {
                type = NavType.StringType
                nullable = true
            },
            navArgument("mealType") {
                type = NavType.StringType
                nullable = true
            }
        )
    ) { backStackEntry ->
        val returnedConsumedAt = backStackEntry.arguments?.getString("consumedAt")
        val returnedMealType = backStackEntry.arguments?.getString("mealType")
        val foodListViewModel =
            hiltViewModel<FoodListViewModel, FoodListViewModel.Factory>(
                creationCallback = { factory ->
                    factory.create(
                        consumedAt = returnedConsumedAt ?: nowYyyyMMDD(),
                        mealType = returnedMealType?.toMealType() ?: MealType.UNSPECIFIED
                    )
                }
            )

        FoodListPage(modifier = modifier, viewModel = foodListViewModel)
    }
}
