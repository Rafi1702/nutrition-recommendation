package com.example.nutritiontracker.ui.home.page

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem as M3NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.nutritiontracker.ui.components.nowYyyyMMDD
import com.example.nutritiontracker.ui.food.page.FoodListPage
import com.example.nutritiontracker.ui.food.page.FoodPath
import com.example.nutritiontracker.ui.food.page.foodGraph
import com.example.nutritiontracker.ui.food.viewmodel.FoodListViewModel
import androidx.compose.ui.platform.LocalInspectionMode
import com.example.nutritiontracker.ui.profile.page.ProfileNav
import com.example.nutritiontracker.ui.profile.page.UserProfilePageRoute
import com.example.nutritiontracker.ui.profile.viewmodel.UserProfileViewModel
import com.example.nutritiontracker.ui.theme.LocalNavController
import com.example.nutritiontracker.ui.theme.LocalUserProfile
import com.example.nutritiontracker.ui.theme.ScreenSize
import com.example.nutritiontracker.ui.theme.Spacing
import com.example.nutritiontracker.ui.theme.colorScheme
import com.example.nutritiontracker.ui.theme.screenSize
import com.example.nutritiontracker.ui.utils.composable.AssistedUserViewModel
import com.example.nutritiontracker.ui.utils.composable.NavigationListener
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
    onNavigateToConsumeLogDetail: () -> Unit = {},
    onNavigateToRecommendedFoodList: () -> Unit = {},
    onNavigateBack: () -> Unit = {},
) {
    val isPreview = LocalInspectionMode.current
    val user = LocalUserProfile.current.value
    Log.d("[USER]", "$user")
    BackHandler { onNavigateBack() }
    val bottomBarNavController: NavHostController? = when {
        isPreview -> null
        screenSize == ScreenSize.MOBILE -> rememberNavController()
        else -> null
    }

    when (screenSize) {
        ScreenSize.MOBILE -> {
            MainScaffold(
                modifier = modifier,
                bottomNavController = bottomBarNavController,
                onNavigateToConsumeLogDetail = onNavigateToConsumeLogDetail,
                onNavigateToRecommendedFoodList = onNavigateToRecommendedFoodList,
            )
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
                    ModalDrawerSheet {
                        Text("Drawer title", modifier = Modifier.padding(Spacing.m))
                        HorizontalDivider()
                        NavigationDrawerItem(
                            label = { Text(text = "Drawer Item") },
                            selected = false,
                            onClick = { }
                        )
                    }
                }) {
                MainScaffold(
                    bottomNavController = bottomBarNavController,
                    drawerProps = drawerRequirementProps,
                    onNavigateToConsumeLogDetail = onNavigateToConsumeLogDetail,
                    onNavigateToRecommendedFoodList = onNavigateToRecommendedFoodList
                )
            }
        }
    }
}

@Composable
private fun MainScaffold(
    modifier: Modifier = Modifier,
    bottomNavController: NavHostController? = null,
    drawerProps: DrawerRequirementProps? = null,
    onNavigateToConsumeLogDetail: () -> Unit = {},
    onNavigateToRecommendedFoodList: () -> Unit = {}
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            AdaptiveTopBar(
                bottomNavController = bottomNavController,
                drawerProps = drawerProps,
            )
        },
        bottomBar = {
            bottomNavController?.let { navController ->
                BottomNavBar(navController = navController)
            }
        }
    ) { innerPadding ->
        MainLayout(
            padding = innerPadding,
            bottomNavController = bottomNavController,
            onNavigateToConsumeLogDetail = onNavigateToConsumeLogDetail,
            onNavigateToRecommendedFoodList = onNavigateToRecommendedFoodList
        )
    }
}

@Composable
private fun MainLayout(
    padding: PaddingValues = PaddingValues(Spacing.none),
    onNavigateToConsumeLogDetail: () -> Unit = {},
    onNavigateToRecommendedFoodList: () -> Unit = {},
    bottomNavController: NavHostController? = null,
) {
    val pickedDate = remember { mutableStateOf(nowYyyyMMDD()) }
    when (screenSize) {
        ScreenSize.MOBILE -> {
            bottomNavController?.let { navController ->

                NavigationListener(navController = navController, tag = "home")

                CompositionLocalProvider(LocalNavController provides navController) {
                    NavHost(
                        navController = navController,
                        startDestination = HomePath.HOME,
                        modifier = Modifier.padding(padding),
                    ) {
                        composable(route = HomePath.HOME) {
                            HomePage(
                                onNavigateToRecommendedFoodList = onNavigateToRecommendedFoodList,
                                onNavigateToConsumeLogDetail = onNavigateToConsumeLogDetail,
                                onDateChanged = { date ->
                                    if (pickedDate.value != date) {
                                        pickedDate.value = date
                                    }
                                }
                            )
                        }

                        composable(ProfileNav.USER_PROFILE) {
                            val userProfileViewModel =
                                AssistedUserViewModel<UserProfileViewModel, UserProfileViewModel.Factory> { factory, userProfile ->
                                    factory.create(userProfile = userProfile)
                                }
                            UserProfilePageRoute(viewModel = userProfileViewModel)
                        }

                        foodGraph(navController = navController)
                    }
                }
            }
        }

        ScreenSize.FOLDABLE, ScreenSize.TABLET -> {
            val foodListViewModel =
                hiltViewModel<FoodListViewModel, FoodListViewModel.Factory>(
                    creationCallback = { factory -> factory.create(consumedAt = pickedDate.value) }
                )
            Row(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Spacing.s),
            ) {
                Spacer(modifier = Modifier.weight(.5f))
                HomePage(
                    modifier = Modifier.weight(2f),
                )
                Spacer(modifier = Modifier.width(Spacing.m))

                FoodListPage(modifier = Modifier.weight(2f), viewModel = foodListViewModel)
                Spacer(modifier = Modifier.weight(.5f))
            }
        }
    }
}

data class TopAppBarComponents(
    val navigationIcon: @Composable (() -> Unit)? = null,
    val title: @Composable () -> Unit,
    val actions: @Composable (() -> Unit)? = null
)

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun AdaptiveTopBar(
    bottomNavController: NavHostController? = null,
    drawerProps: DrawerRequirementProps? = null,
) {
    val navBackStackEntry = bottomNavController?.currentBackStackEntryAsState()?.value
    val currentRoute = navBackStackEntry?.destination?.route

    val components = when {
        currentRoute == HomePath.HOME -> TopAppBarComponents(title = { Text("Home") }, actions = {
            IconButton(onClick = {}) {
                Icon(Icons.Default.Settings, contentDescription = "settings_button")
            }
        })

        currentRoute?.startsWith(FoodPath.FOOD_LIST_BASE) == true -> TopAppBarComponents(title = {
            Text(
                "Food List"
            )
        })

        currentRoute == ProfileNav.USER_PROFILE -> TopAppBarComponents(title = {
            Text(
                "Profile"
            )
        })

        else -> TopAppBarComponents({}, {}, {})
    }

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
            components.title()
        }
    }, actions = { components.actions?.invoke() })
}

@Preview
@Composable
private fun BottomNavBar(
    navController: NavHostController? = null,
) {
    val navBackStackEntry = navController?.currentBackStackEntryAsState()?.value
    val currentRoute = navBackStackEntry?.destination?.route

    navController?.let { controller ->
        NavigationBar(containerColor = colorScheme.surfaceContainer) {
            M3NavigationBarItem(
                selected = currentRoute == HomePath.HOME,
                label = { Text("Home") },
                alwaysShowLabel = true,
                onClick = {
                    if (currentRoute != HomePath.HOME) {
                        controller.navigate(HomePath.HOME) {
                            popUpTo(controller.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = {
                    Icon(Icons.Default.Home, contentDescription = "navigation_home")
                }
            )

            val isFoodSelected = currentRoute?.startsWith(FoodPath.FOOD_LIST_BASE) == true
            M3NavigationBarItem(
                selected = isFoodSelected,
                alwaysShowLabel = true,
                label = { Text("Food") },
                onClick = {
                    val targetRoute = FoodPath.createFoodListRoute()
                    if (currentRoute != targetRoute) {
                        controller.navigate(targetRoute) {
                            popUpTo(controller.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = {
                    Icon(Icons.Default.RestaurantMenu, contentDescription = "navigation_food")
                }
            )

            M3NavigationBarItem(
                selected = currentRoute == ProfileNav.USER_PROFILE,
                alwaysShowLabel = true,
                label = { Text("Profile") },
                onClick = {
                    if (currentRoute != ProfileNav.USER_PROFILE) {
                        controller.navigate(ProfileNav.USER_PROFILE) {
                            popUpTo(controller.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = {
                    Icon(Icons.Default.Person, contentDescription = "navigation_profile")
                }
            )
        }
    }
}
