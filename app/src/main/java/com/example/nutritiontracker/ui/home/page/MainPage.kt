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
import androidx.compose.material.icons.filled.Menu
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
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.nutritiontracker.data.model.consumelog.toMealType
import com.example.nutritiontracker.domain.model.MealType
import com.example.nutritiontracker.ui.components.nowYyyyMMDD
import com.example.nutritiontracker.ui.food.page.FoodListPage
import com.example.nutritiontracker.ui.food.page.FoodPath
import com.example.nutritiontracker.ui.food.page.FoodRecommendationsPage
import com.example.nutritiontracker.ui.food.viewmodel.FoodListViewModel
import com.example.nutritiontracker.ui.food.viewmodel.FoodRecommendationViewModel
import com.example.nutritiontracker.ui.home.components.DialogEvent
import com.example.nutritiontracker.ui.home.components.FoodSelectionConfirmation
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
import androidx.compose.material3.NavigationBarItem as M3NavigationBarItem

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

    //This state passed to FoodList composable (not ViewModel)
    var isSelectedFoodsNotEmpty by remember { mutableStateOf<Boolean?>(null) }
    var pendingRoute by remember { mutableStateOf<String?>(null) }
    val pickedDate = remember { mutableStateOf(nowYyyyMMDD()) }

    val showFoodConfirmationDialog =
        isSelectedFoodsNotEmpty == true && pendingRoute != null

    fun navigateTo(route: String) {
        val normalizedCurrentRoute =
            bottomNavController?.currentBackStackEntry?.destination?.route?.let { route ->
                when {
                    route.startsWith(FoodPath.FOOD_LIST_BASE) -> FoodPath.createFoodListRoute(
                        consumedAt = pickedDate.value,
                        mealType = MealType.UNSPECIFIED.label
                    )

                    else -> route
                }
            }

        val targetRoute = when {
            route.startsWith(FoodPath.FOOD_LIST_BASE) ->
                FoodPath.createFoodListRoute(
                    consumedAt = pickedDate.value,
                    mealType = MealType.UNSPECIFIED.label
                )

            else -> route
        }
        if (normalizedCurrentRoute == targetRoute) return

        if (isSelectedFoodsNotEmpty == true) {
            pendingRoute = targetRoute
        } else {
            bottomNavController?.navigate(targetRoute)
        }
    }


    fun confirmNavigation() {
        isSelectedFoodsNotEmpty = false

        pendingRoute?.let { route ->
            bottomNavController?.navigate(route)
        }

        pendingRoute = null
    }


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
                BottomNavBar(
                    navController = navController,
                    onNavBarItemPressed = ::navigateTo
                )
            }
        }
    ) { innerPadding ->
        MainLayout(
            padding = innerPadding,
            bottomNavController = bottomNavController,
            onNavigateToConsumeLogDetail = onNavigateToConsumeLogDetail,
            showFoodConfirmationDialog = showFoodConfirmationDialog,
            pickedDate = pickedDate.value,
            onPickedDateChanged = { newDate ->
                pickedDate.value = newDate
            },
            confirmationDialogEvent = object : DialogEvent {
                override fun onDismissRequest() {
                    pendingRoute = null
                }

                override fun onConfirm() {
                    confirmNavigation()
                }

            },
            onSelectedFoodPicked = { isFoodsPicked -> isSelectedFoodsNotEmpty = isFoodsPicked },
            isSelectedFoodsNotEmpty = isSelectedFoodsNotEmpty,
            onNavigateToRecommendedFoodList = onNavigateToRecommendedFoodList
        )
    }
}

@Composable
private fun MainLayout(
    padding: PaddingValues = PaddingValues(Spacing.none),
    onNavigateToConsumeLogDetail: () -> Unit = {},
    onNavigateToRecommendedFoodList: () -> Unit = {},
    showFoodConfirmationDialog: Boolean? = null,
    isSelectedFoodsNotEmpty: Boolean? = null,
    confirmationDialogEvent: DialogEvent,
    pickedDate: String = nowYyyyMMDD(),
    onPickedDateChanged: (String) -> Unit = {},
    onSelectedFoodPicked: (Boolean) -> Unit = {},
    bottomNavController: NavHostController? = null,
) {


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
                                pickedDate = pickedDate,
                                onDateChanged = { date ->
                                    if (pickedDate != date) {
                                        onPickedDateChanged(date)
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

                        composable(route = FoodPath.FOOD_RECOMMENDATIONS) {
                            val foodRecommendationViewModel: FoodRecommendationViewModel =
                                hiltViewModel()
                            FoodRecommendationsPage(viewModel = foodRecommendationViewModel)
                        }

                        composable(
                            route = FoodPath.FOOD_LIST_ROUTE,
                            arguments = listOf(
                                navArgument("consumedAt") {
                                    type = NavType.StringType
                                    nullable = true
                                },
                                navArgument("mealType") {
                                    type = NavType.StringType
                                    nullable = true
                                }
                            )
                        ) { backStackEntry ->
                            val returnedConsumedAt =
                                backStackEntry.arguments?.getString("consumedAt")
                            val returnedMealType = backStackEntry.arguments?.getString("mealType")
                            val foodListViewModel =
                                hiltViewModel<FoodListViewModel, FoodListViewModel.Factory>(
                                    creationCallback = { factory ->
                                        factory.create(
                                            consumedAt = returnedConsumedAt ?: nowYyyyMMDD(),
                                            mealType = returnedMealType?.toMealType()
                                                ?: MealType.UNSPECIFIED
                                        )
                                    }
                                )

                            FoodListPage(
                                viewModel = foodListViewModel,
                                onSelectedFoodPicked = { isSelectedFoods ->
                                    onSelectedFoodPicked(isSelectedFoods)
                                })
                        }
                    }
                }

                if (isSelectedFoodsNotEmpty == true && showFoodConfirmationDialog == true) {
                    FoodSelectionConfirmation(confirmationDialogEvent)
                }
            }
        }

        ScreenSize.FOLDABLE, ScreenSize.TABLET -> {
            val foodListViewModel =
                hiltViewModel<FoodListViewModel, FoodListViewModel.Factory>(
                    creationCallback = { factory -> factory.create(consumedAt = pickedDate) }
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
                    pickedDate = pickedDate
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

@Composable
private fun BottomNavBar(
    onNavBarItemPressed: (currentRoute: String) -> Unit = {},
    navController: NavHostController
) {
    navController.let { controller ->
        NavigationBar(containerColor = colorScheme.surfaceContainer) {
            MainNavigation.entries.forEach { entry ->
                val props = entry.toMainNavigationProps(controller)
                M3NavigationBarItem(
                    selected = props.selected,
                    label = { Text(props.label) },
                    onClick = {
                        onNavBarItemPressed(props.route)
                    },
                    icon = {
                        Icon(
                            props.icon,
                            contentDescription = "main_bottom_nav_bar_${props.label.lowercase()}"
                        )
                    })
            }

        }
    }
}



