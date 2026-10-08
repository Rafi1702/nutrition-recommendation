package com.example.nutritiontracker.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.nutritiontracker.ui.authentication.page.AuthPage
import com.example.nutritiontracker.ui.authentication.page.AuthPath
import com.example.nutritiontracker.ui.authentication.viewmodel.AuthenticationViewModel
import com.example.nutritiontracker.ui.home.page.HomePath
import com.example.nutritiontracker.ui.home.page.MainPage

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = AuthPath.AUTH
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
    ) {

        composable(AuthPath.AUTH) {
            val authenticationViewModel: AuthenticationViewModel = hiltViewModel()
            AuthPage(authViewModel = authenticationViewModel)
        }
        composable(HomePath.BASE_HOME_PATH) {
            MainPage()
        }
    }
}
