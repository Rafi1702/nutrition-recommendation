package com.example.nutritiontracker.ui.home.page

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation


object HomePath{
    const val BASE_HOME_PATH = "/home"
    const val HOME = "/"
    const val CONSUME_LOG = "/consume"
}
fun NavGraphBuilder.homeGraph(navController: NavHostController) {
    navigation(route = HomePath.BASE_HOME_PATH, startDestination = HomePath.HOME) {
        composable(route = HomePath.HOME) {
            HomePage(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
        composable(route = HomePath.CONSUME_LOG) {
            ConsumeLogs()
        }
    }
}