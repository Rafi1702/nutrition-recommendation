package com.example.nutritiontracker.ui.home.page

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.nutritiontracker.ui.food.page.FoodRecommendationsContent
import com.example.nutritiontracker.ui.theme.LocalNavController
import com.example.nutritiontracker.ui.theme.ScreenSize
import com.example.nutritiontracker.ui.theme.Spacing
import com.example.nutritiontracker.ui.theme.colorScheme
import com.example.nutritiontracker.ui.theme.screenSize

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun MainPage(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit = {}
) {

    val bottomBarNavController = if (screenSize == ScreenSize.MOBILE) {
        rememberNavController()
    } else {
        null
    }

    BackHandler {
        onNavigateBack()
    }
    CompositionLocalProvider(LocalNavController provides bottomBarNavController) {
        Scaffold(
            topBar = {
                AdaptiveTopBar()
            },
        ) { innerPadding ->
            MainLayout(padding = innerPadding)
        }
    }
}

@Composable
private fun MainLayout(padding: PaddingValues = PaddingValues(Spacing.none)) {
    when (screenSize) {
        ScreenSize.MOBILE -> {
            val navController = LocalNavController.current

            navController?.let {
                NavHost(
                    navController = navController,
                    startDestination = HomePath.BASE_HOME_PATH
                ) {
                    homeGraph(
                        modifier = Modifier.padding(padding),
                    )
                }
            }
        }

        ScreenSize.FOLDABLE, ScreenSize.TABLET -> {
            Row(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Spacing.s),
            ) {
                Spacer(modifier = Modifier.weight(.5f))
                HomePage(
                    modifier = Modifier
                        .weight(2f)
                )
                FoodRecommendationsContent(modifier = Modifier.weight(2f))
                Spacer(modifier = Modifier.weight(.5f))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun AdaptiveTopBar() {
    val navController = LocalNavController.current

    val navBackStackEntry = LocalNavController.current?.currentBackStackEntryAsState()?.value
    val currentRoute = navBackStackEntry?.destination?.route

    if (navController != null) {
        val title = remember(currentRoute) {
            when (currentRoute) {
                HomePath.HOME -> "Home"
                HomePath.CONSUME_LOG -> "Consume"
                else -> null
            }
        }
        TopAppBar(title = {
            title?.let {
                Text(it)
            }
        })
    }
}

@Preview
@Composable
private fun BottomNavBar(

) {

    val navController = LocalNavController.current
    val navBackStackEntry = LocalNavController.current?.currentBackStackEntryAsState()?.value
    val currentRoute = navBackStackEntry?.destination?.route

    navController?.let {
        NavigationBar(containerColor = colorScheme.surfaceContainer) {
            NavigationBarItem(selected = currentRoute == HomePath.HOME, onClick = {
                navController.navigate(HomePath.HOME) {
                    launchSingleTop = true
                    popUpTo(HomePath.HOME)
                }
            }, icon = {
                Icon(Icons.Default.Home, contentDescription = "navigation_home")
            })

            NavigationBarItem(
                selected = currentRoute == HomePath.CONSUME_LOG,
                onClick = {
                    navController.navigate(HomePath.CONSUME_LOG) {
                        launchSingleTop = true
                        popUpTo(HomePath.HOME)
                    }
                }, icon = {
                    Icon(Icons.Default.Home, contentDescription = "navigation_home")
                })
        }
    }
}
