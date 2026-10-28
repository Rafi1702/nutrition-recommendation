package com.example.nutritiontracker

import android.app.Application
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.compose.rememberNavController
import com.example.nutritiontracker.data.utils.NetworkException
import com.example.nutritiontracker.ui.AppNavigation
import com.example.nutritiontracker.ui.authentication.AuthEventManager
import com.example.nutritiontracker.ui.authentication.page.AuthPath
import com.example.nutritiontracker.ui.theme.NutritionTrackerTheme
import com.example.nutritiontracker.ui.theme.snackBarHostState
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class CoreApplication : Application()

@RequiresApi(Build.VERSION_CODES.O)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            NutritionTrackerTheme {
                val navController = rememberNavController()
                val snackBarHostState = snackBarHostState
                //Observe global AuthEvent. it trigger specific error to navigate back to Sign In
                LaunchedEffect(Unit) {
                    AuthEventManager.authEvent.collect { err ->
                        if (err is NetworkException.Unauthorized || err is NetworkException.GenericHttpError) {
                            navController.navigate(AuthPath.AUTH)
                            snackBarHostState.showSnackbar(
                                message = "Unauthorized Access",
                            )
                        }
                    }
                }

                AppNavigation(
                    navController = navController,
                    snackBarHostState = snackBarHostState
                )
            }
        }
    }
}







