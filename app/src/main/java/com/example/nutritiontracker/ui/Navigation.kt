package com.example.nutritiontracker.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.nutritiontracker.data.utils.NetworkException
import com.example.nutritiontracker.domain.model.UserProfile
import com.example.nutritiontracker.ui.authentication.AuthEventManager
import com.example.nutritiontracker.ui.authentication.page.AuthPageRoute
import com.example.nutritiontracker.ui.authentication.page.AuthPath
import com.example.nutritiontracker.ui.authentication.viewmodel.AuthenticationViewModel
import com.example.nutritiontracker.ui.consumelog.page.ConsumeLogNav
import com.example.nutritiontracker.ui.consumelog.page.consumeGraph
import com.example.nutritiontracker.ui.food.page.FoodPath
import com.example.nutritiontracker.ui.home.page.HomePath
import com.example.nutritiontracker.ui.home.page.MainPage
import com.example.nutritiontracker.ui.profile.page.ProfileNav
import com.example.nutritiontracker.ui.profile.page.UserProfilePageRoute
import com.example.nutritiontracker.ui.profile.viewmodel.UserProfileViewModel
import com.example.nutritiontracker.ui.theme.LocalUserProfile
import com.example.nutritiontracker.ui.theme.snackBarHostState

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = "/",
    snackBarHostState: SnackbarHostState,
) {
    val entryViewModel: EntryScreenViewModel = hiltViewModel()
    val entryScreenUiState = entryViewModel.entryNavigationState.collectAsStateWithLifecycle().value
    val userProfileState = remember { mutableStateOf<UserProfile?>(null) }

    //Observe the effect of the state changes on EntryScreenViewmodel
    LaunchedEffect(entryScreenUiState) {
        when (entryScreenUiState) {
            is EntryScreenUiState.Navigate -> {
                val (route, data) = entryScreenUiState
                navController.navigate(route)
                data?.let {
                    userProfileState.value = data
                }
            }

            else -> {}
        }
    }


    CompositionLocalProvider(LocalUserProfile provides userProfileState) {
        Scaffold(snackbarHost = { SnackbarHost(snackBarHostState) }) { _ ->
            NavHost(
                navController = navController,
                startDestination = startDestination,
                modifier = modifier,
            ) {

                composable("/") {
                    EntryScreen(entryScreenUiState)
                }

                composable(AuthPath.AUTH) {
                    val authenticationViewModel: AuthenticationViewModel = hiltViewModel()
                    AuthPageRoute(
                        viewModel = authenticationViewModel,
                        onNavigateToHome = { profile ->
                            if (profile != null) {
                                userProfileState.value = profile
                                navController.navigate(HomePath.BASE_HOME_PATH)
                            } else {
                                navController.navigate(ProfileNav.USER_PROFILE)
                            }

                        })
                }

                composable(route = HomePath.BASE_HOME_PATH) {
                    MainPage(
                        modifier = modifier,
                        onNavigateBack = { },
                        onNavigateToConsumeLogDetail = {
                            navController.navigate(
                                ConsumeLogNav.ConsumeLogDetail(
                                    "0"
                                ).route
                            )
                        },
                        onNavigateToRecommendedFoodList = { navController.navigate(FoodPath.FOOD_RECOMMENDATIONS) }
                    )
                }

                consumeGraph(onNavigateBack = {
                    navController.popBackStack()
                })

                composable(ProfileNav.USER_PROFILE) {
                    val currentUser = LocalUserProfile.current.value
                    val userProfileViewModel =
                        hiltViewModel<UserProfileViewModel, UserProfileViewModel.Factory>(
                            creationCallback = { factory ->
                                factory.create(userProfile = currentUser)
                            }
                        )
                    UserProfilePageRoute(viewModel = userProfileViewModel)
                }
            }
        }
    }
}
