package com.example.nutritiontracker.ui.home.page

import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation


object HomePath {
    const val BASE_HOME_PATH = "/home"
    const val HOME = "/"
    const val CONSUME_LOG = "/consume"
    const val FOOD_RECOMMENDATIONS = "/food_recommendation"
}

fun NavGraphBuilder.homeGraph(modifier: Modifier = Modifier, navController: NavHostController) {
    navigation(route = HomePath.BASE_HOME_PATH, startDestination = HomePath.HOME) {
        composable(route = HomePath.HOME) {
            HomePage(modifier = modifier)
        }
        composable(route = HomePath.FOOD_RECOMMENDATIONS){

        }
    }
}