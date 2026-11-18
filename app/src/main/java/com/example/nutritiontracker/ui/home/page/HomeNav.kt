package com.example.nutritiontracker.ui.home.page

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dangerous
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.nutritiontracker.ui.food.page.FoodPath
import com.example.nutritiontracker.ui.profile.page.ProfileNav

object HomePath {
    const val BASE_HOME_PATH = "/main"
    const val HOME = "/home"
}



enum class MainNavigation(
    val label: String,
    val route: String,
    val icon: ImageVector,
) {
    HOME("Home", HomePath.HOME, Icons.Default.Home),
    FOOD_LIST("Food List", FoodPath.FOOD_LIST_BASE, Icons.Default.RestaurantMenu),
    USER_PROFILE("User Profile", ProfileNav.USER_PROFILE, Icons.Default.Person);
}


@Composable
fun MainNavigation.toMainNavigationProps(navController: NavHostController): MainNavigationBarProps {
    val navBackStackEntry = navController.currentBackStackEntryAsState().value
    val currentRoute = navBackStackEntry?.destination?.route
    return when (val entry = this) {
        MainNavigation.HOME -> MainNavigationBarProps(
            selected = currentRoute == entry.route,
            route = entry.route,
            label = entry.label,
            icon = entry.icon,
            onClick = {
                navController.navigate(entry.route) {
                    popUpTo(navController.graph.startDestinationId)
                    launchSingleTop = true
                    restoreState = true
                }
            }
        )

        MainNavigation.FOOD_LIST -> {
            val isFoodSelected = currentRoute?.startsWith(entry.route) == true
            val targetRoute = FoodPath.createFoodListRoute()
            MainNavigationBarProps(
                selected = isFoodSelected,
                label = entry.label,
                route = entry.route,
                icon = entry.icon,
                onClick = {
                    navController.navigate(targetRoute) {
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }

        MainNavigation.USER_PROFILE -> MainNavigationBarProps(
            selected = currentRoute == entry.route,
            label = entry.label,
            route = entry.route,
            icon = entry.icon,
            onClick = {
                navController.navigate(entry.route) {
                    popUpTo(navController.graph.startDestinationId)
                    launchSingleTop = true
                    restoreState = true
                }
            }
        )
    }
}

data class MainNavigationBarProps(
    val route: String,
    val label: String,
    val selected: Boolean = false,
    val onClick: (() -> Unit)? = null,
    val icon: ImageVector = Icons.Default.Dangerous
)