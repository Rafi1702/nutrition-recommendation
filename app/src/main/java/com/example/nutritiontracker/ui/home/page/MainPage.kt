package com.example.nutritiontracker.ui.home.page

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.nutritiontracker.ui.food.page.FoodRecommendationsContent
import com.example.nutritiontracker.ui.theme.ScreenSize
import com.example.nutritiontracker.ui.theme.Spacing
import com.example.nutritiontracker.ui.theme.colorScheme
import com.example.nutritiontracker.ui.theme.screenSize
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


data class DrawerRequirementProps(
    val drawerState: DrawerState,
    val coroutineScope: CoroutineScope,
)

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun MainPage(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit = {}
) {
    BackHandler { onNavigateBack() }
    when (screenSize) {
        ScreenSize.MOBILE -> {
            val bottomBarNavController = rememberNavController()
            MainScaffold(bottomNavController = bottomBarNavController)
        }

        else -> {

            val drawerRequirementProps =
                DrawerRequirementProps(
                    drawerState = rememberDrawerState(initialValue = DrawerValue.Closed),
                    coroutineScope = rememberCoroutineScope()
                )
            ModalNavigationDrawer(
                drawerState = drawerRequirementProps.drawerState,
                drawerContent = {
                    ModalDrawerSheet { /* Drawer content */
                        Text("Drawer title", modifier = Modifier.padding(Spacing.m))
                        HorizontalDivider()
                        NavigationDrawerItem(
                            label = { Text(text = "Drawer Item") },
                            selected = false,
                            onClick = { /*TODO*/ }
                        )
                    }
                }) {
                MainScaffold(
                    drawerProps = drawerRequirementProps
                )
            }
        }
    }
}


@Composable
private fun MainScaffold(
    bottomNavController: NavHostController? = null,
    drawerProps: DrawerRequirementProps? = null,
) {
    Scaffold(
        topBar = {
            AdaptiveTopBar(
                bottomNavController = bottomNavController,
                drawerProps = drawerProps,
            )
        },

        bottomBar = { BottomNavBar(navController = bottomNavController) }
    ) { innerPadding ->
        MainLayout(padding = innerPadding, bottomNavController)
    }
}

@Composable
private fun MainLayout(
    padding: PaddingValues = PaddingValues(Spacing.none),
    bottomNavController: NavHostController? = null
) {
    when (screenSize) {
        ScreenSize.MOBILE -> {

            bottomNavController?.let {
                NavHost(
                    navController = bottomNavController,
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
                        .weight(2f),
                    navController = bottomNavController
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
private fun AdaptiveTopBar(
    bottomNavController: NavHostController? = null,
    drawerProps: DrawerRequirementProps? = null,
) {
    val navBackStackEntry = bottomNavController?.currentBackStackEntryAsState()?.value
    val currentRoute = navBackStackEntry?.destination?.route

    TopAppBar(title = {

        drawerProps?.let {
            val (drawerState, coroutineScope) = it
            Icon(Icons.Default.Menu, modifier = Modifier.clickable {
                drawerState.apply {
                    coroutineScope.launch {
                        if (isClosed) open() else close()
                    }
                }
            }, contentDescription = "side_bar_menu")
        }
        if (bottomNavController != null) {
            val title = remember(currentRoute) {
                when (currentRoute) {
                    HomePath.HOME -> "Home"
                    HomePath.CONSUME_LOG -> "Consume"
                    else -> null
                }
            }
            title?.let {
                Text(it)
            }
        }
    })
}

@Preview
@Composable
private fun BottomNavBar(
    navController: NavHostController? = null
) {
    val navController = navController
    val navBackStackEntry = navController?.currentBackStackEntryAsState()?.value
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
