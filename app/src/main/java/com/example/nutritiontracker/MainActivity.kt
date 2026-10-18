package com.example.nutritiontracker

import android.app.Application
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.navigation.compose.rememberNavController
import com.example.nutritiontracker.data.local.LocalTokenDataSource
import com.example.nutritiontracker.ui.AppNavigation
import com.example.nutritiontracker.ui.authentication.page.AuthPath
import com.example.nutritiontracker.ui.home.page.HomePath
import com.example.nutritiontracker.ui.theme.NutritionTrackerTheme
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject


@HiltAndroidApp
class CoreApplication : Application()

@RequiresApi(Build.VERSION_CODES.O)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var localTokenDataSource: LocalTokenDataSource
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val (accessToken, refreshToken) =  localTokenDataSource.getUserToken()
        enableEdgeToEdge()
        setContent {
            NutritionTrackerTheme {
                val navController = rememberNavController()

                val startDestination = when {
                    accessToken != null && refreshToken != null -> HomePath.BASE_HOME_PATH
                    else -> AuthPath.AUTH
                }

                AppNavigation(
                    navController = navController,
                    startDestination = startDestination
                )
            }
        }
    }
}







