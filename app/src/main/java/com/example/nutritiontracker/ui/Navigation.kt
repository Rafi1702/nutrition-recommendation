package com.example.nutritiontracker.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.nutritiontracker.ui.authentication.page.AuthPageRoute
import com.example.nutritiontracker.ui.authentication.page.AuthPath
import com.example.nutritiontracker.ui.authentication.viewmodel.AuthenticationViewModel
import com.example.nutritiontracker.ui.home.page.HomePath
import com.example.nutritiontracker.ui.home.page.MainPage
import com.example.nutritiontracker.ui.profile.page.ProfileNav
import com.example.nutritiontracker.ui.profile.page.UserProfilePageRoute
import com.example.nutritiontracker.ui.profile.viewmodel.UserProfileViewModel

@RequiresApi(Build.VERSION_CODES.O)
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
            AuthPageRoute(viewModel = authenticationViewModel, onNavigateToHome = {
                navController.navigate(HomePath.BASE_HOME_PATH)
            })
        }
        composable(HomePath.BASE_HOME_PATH) {
            MainPage()
        }

        composable(ProfileNav.USER_PROFILE) {
            val userProfileViewModel: UserProfileViewModel = hiltViewModel()
            UserProfilePageRoute(viewModel = userProfileViewModel)
        }
    }
}
