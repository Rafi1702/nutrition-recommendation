package com.example.nutritiontracker.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.nutritiontracker.data.utils.NetworkException
import com.example.nutritiontracker.ui.authentication.AuthEventManager
import com.example.nutritiontracker.ui.authentication.page.AuthPageRoute
import com.example.nutritiontracker.ui.authentication.page.AuthPath
import com.example.nutritiontracker.ui.authentication.viewmodel.AuthenticationViewModel
import com.example.nutritiontracker.ui.consumelog.page.ConsumeLogNav
import com.example.nutritiontracker.ui.consumelog.page.consumeGraph
import com.example.nutritiontracker.ui.food.page.FoodPath
import com.example.nutritiontracker.ui.home.page.HomePath
import com.example.nutritiontracker.ui.home.page.homeGraph
import com.example.nutritiontracker.ui.profile.page.ProfileNav
import com.example.nutritiontracker.ui.profile.page.UserProfilePageRoute
import com.example.nutritiontracker.ui.profile.viewmodel.UserProfileViewModel
import com.example.nutritiontracker.ui.theme.snackBarHostState

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = "ENTRY_SCREEN",
) {
    val entryViewModel: EntryScreenViewModel = hiltViewModel()
    val entryScreenUiState = entryViewModel.entryNavigationState.collectAsStateWithLifecycle().value
    val snackBarHostState = snackBarHostState


    LaunchedEffect(entryScreenUiState) {
        when (entryScreenUiState) {
            is EntryScreenUiState.Navigate -> {
                navController.navigate(entryScreenUiState.route)
            }

            else -> {}
        }
    }

    LaunchedEffect(Unit) {
        AuthEventManager.authEvent.collect { err ->
            if(err is NetworkException.Unauthorized){
                navController.navigate(AuthPath.AUTH)
                snackBarHostState.showSnackbar(
                    message = "Unauthorized Access",
                )
            }
        }
    }

    Scaffold(snackbarHost = { SnackbarHost(snackBarHostState) }) { _ ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = modifier,
        ) {

            composable("ENTRY_SCREEN") {
                EntryScreen(entryScreenUiState)
            }

            composable(AuthPath.AUTH) {
                val authenticationViewModel: AuthenticationViewModel = hiltViewModel()
                AuthPageRoute(viewModel = authenticationViewModel, onNavigateToHome = {
                    navController.navigate(HomePath.BASE_HOME_PATH)
                })
            }

            homeGraph(
                onNavigateToHome = {
                    navController.navigate(route = HomePath.HOME)
                },
                onNavigateToRecommendedFoodList = {
                    navController.navigate(FoodPath.FOOD_RECOMMENDATIONS)
                },
                onNavigateToConsumeLogDetail = {
                    navController.navigate(ConsumeLogNav.ConsumeLogDetail("0").route)
                }
            )

            consumeGraph(onNavigateBack = {
                navController.popBackStack()
            })

            composable(ProfileNav.USER_PROFILE) {
                val userProfileViewModel: UserProfileViewModel = hiltViewModel()
                UserProfilePageRoute(viewModel = userProfileViewModel)
            }
        }
    }
}
