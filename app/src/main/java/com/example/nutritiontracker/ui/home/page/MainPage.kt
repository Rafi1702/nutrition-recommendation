package com.example.nutritiontracker.ui.home.page

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import com.example.nutritiontracker.ui.theme.LocalNavController
import com.example.nutritiontracker.ui.theme.ScreenSize
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
            bottomBar = {
                BottomNavBar()
            }
        ) { innerPadding ->
            MainLayout(padding = innerPadding)
        }
    }
}


@Composable
private fun MainLayout(padding: PaddingValues = PaddingValues(0.dp)) {
    val navController = LocalNavController.current

    if (navController != null) {
        NavHost(
            navController = navController,
            startDestination = HomePath.BASE_HOME_PATH
        ) {
            homeGraph(
                modifier = Modifier.padding(padding),
                navController = navController
            )
        }
    } else {

        Row {
            if (screenSize == ScreenSize.TABLET) Spacer(modifier = Modifier.weight(1f))
            Home(
                modifier = Modifier
                    .weight(2f)
                    .padding(padding)
            )
            ConsumeLogs(
                modifier = Modifier
                    .weight(2f)
                    .fillMaxHeight()
                    .padding(padding)
            )
            if (screenSize == ScreenSize.TABLET) Spacer(modifier = Modifier.weight(1f))
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

    if (navController != null) {
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
