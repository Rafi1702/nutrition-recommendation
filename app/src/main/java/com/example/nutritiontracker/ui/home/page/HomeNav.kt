package com.example.nutritiontracker.ui.home.page

import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.nutritiontracker.ui.food.page.FoodListPage
import com.example.nutritiontracker.ui.food.page.foodGraph

object HomePath {
    const val BASE_HOME_PATH = "/main"
    const val HOME = "/home"
    const val CONSUME_LOG = "/consume"
    const val FOOD_LIST = "/foods"
}

fun NavGraphBuilder.homeGraph(
    modifier: Modifier = Modifier,
    onNavigateToHome: () -> Unit = {},
) {
    navigation(
        startDestination = HomePath.HOME,
        route = HomePath.BASE_HOME_PATH
    ) {
        composable(route = HomePath.HOME) {
            MainPage(
                modifier = modifier,
                onNavigateBack = onNavigateToHome,
            )
        }
    }
}
