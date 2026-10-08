package com.example.nutritiontracker

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.example.nutritiontracker.ui.AppNavigation
import com.example.nutritiontracker.ui.theme.NutritionTrackerTheme
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class CoreApplication: Application()

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NutritionTrackerTheme {
                val navController = rememberNavController()
                AppNavigation(navController = navController)
            }
        }
    }
}







