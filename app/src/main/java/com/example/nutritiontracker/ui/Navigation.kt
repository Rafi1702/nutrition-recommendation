package com.example.nutritiontracker.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.nutritiontracker.ui.authentication.page.AuthPath
import com.example.nutritiontracker.ui.authentication.page.LoginPage
import com.example.nutritiontracker.ui.home.page.HomePath
import com.example.nutritiontracker.ui.home.page.homeGraph


@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = AuthPath.AUTH
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        route = AuthPath.BASE_AUTH_PATH,
    ) {
        composable(AuthPath.AUTH) {
            LoginPage(modifier = modifier, onNavigateToHome = {
                navController.navigate(route = HomePath.HOME)
            })
        }

        homeGraph(navController = navController)
    }
}
