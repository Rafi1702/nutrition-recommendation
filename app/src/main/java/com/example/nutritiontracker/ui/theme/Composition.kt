package com.example.nutritiontracker.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import com.example.nutritiontracker.domain.model.UserProfile

val LocalTypography = compositionLocalOf { phoneTypography }

val typography: Typography
    @Composable
    get() = rememberAppTypography(LocalScreenSize.current)


val LocalColorScheme = compositionLocalOf { LightColorScheme }

val colorScheme: ColorScheme
    @Composable
    @ReadOnlyComposable
    get() = LocalColorScheme.current

val LocalScreenSize = compositionLocalOf { ScreenSize.MOBILE }

val screenSize: ScreenSize
    @Composable
    get() = rememberScreenSize()

val LocalNavController =
    compositionLocalOf<NavHostController?> { error("No NavHostController Provided") }

val LocalSnackBarHost = compositionLocalOf<SnackbarHostState> { error("No SnackBar Host Provided") }

val snackBarHostState: SnackbarHostState
    @Composable
    get() = remember { SnackbarHostState() }

val LocalUserProfile =
    compositionLocalOf<MutableState<UserProfile?>> { mutableStateOf(null) }

val userProfile: UserProfile?
    @Composable
    get() = LocalUserProfile.current.value

