package com.example.nutritiontracker.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

val PrimaryLight = Color(0xFF2E7D32)
val OnPrimaryLight = Color(0xFFFFFFFF)
val PrimaryContainerLight = Color(0xFFA5D6A7)
val OnPrimaryContainerLight = Color(0xFF002106)

val SecondaryLight = Color(0xFFE65100)
val OnSecondaryLight = Color(0xFFFFFFFF)
val SecondaryContainerLight = Color(0xFFFFCCBC)
val OnSecondaryContainerLight = Color(0xFF351000)

val TertiaryLight = Color(0xFF00695C)
val OnTertiaryLight = Color(0xFFFFFFFF)

val BackgroundLight = Color(0xFFF8FBF8)
val SurfaceLight = Color(0xFFFFFFFF)
val OnBackgroundLight = Color(0xFF191C19)
val OnSurfaceLight = Color(0xFF191C19)
val SurfaceVariantLight = Color(0xFFE2EBE2)
val OnSurfaceVariantLight = Color(0xFF424942)

// --- Extended Color Tokens (Light) ---
val SuccessLight = Color(0xFF2E7D32)
val OnSuccessLight = Color(0xFFFFFFFF)
val SuccessContainerLight = Color(0xFFC8E6C9)
val OnSuccessContainerLight = Color(0xFF00390A)

val WarningLight = Color(0xFFED6C02)
val OnWarningLight = Color(0xFFFFFFFF)
val WarningContainerLight = Color(0xFFFFE0B2)
val OnWarningContainerLight = Color(0xFF371E00)

val ErrorLight = Color(0xFFBA1A1A)
val OnErrorLight = Color(0xFFFFFFFF)
val ErrorContainerLight = Color(0xFFFFDAD6)
val OnErrorContainerLight = Color(0xFF410002)

// --- Dark Theme Colors ---
val PrimaryDark = Color(0xFF81C784)
val OnPrimaryDark = Color(0xFF003910)
val PrimaryContainerDark = Color(0xFF00531C)
val OnPrimaryContainerDark = Color(0xFFA5D6A7)

val SecondaryDark = Color(0xFFFFAB91)
val OnSecondaryDark = Color(0xFF5C1A00)
val SecondaryContainerDark = Color(0xFF832A00)
val OnSecondaryContainerDark = Color(0xFFFFCCBC)

val TertiaryDark = Color(0xFF4DB6AC)
val OnTertiaryDark = Color(0xFF003732)

val BackgroundDark = Color(0xFF111411)
val SurfaceDark = Color(0xFF111411)
val OnBackgroundDark = Color(0xFFE2E3DF)
val OnSurfaceDark = Color(0xFFE2E3DF)
val SurfaceVariantDark = Color(0xFF1F2620)
val OnSurfaceVariantDark = Color(0xFFC1C7C1)

// --- Extended Color Tokens (Dark) ---
val SuccessDark = Color(0xFF81C784)
val OnSuccessDark = Color(0xFF00390A)
val SuccessContainerDark = Color(0xFF1B5E20)
val OnSuccessContainerDark = Color(0xFFC8E6C9)

val WarningDark = Color(0xFFFFB74D)
val OnWarningDark = Color(0xFF452200)
val WarningContainerDark = Color(0xFFB25900)
val OnWarningContainerDark = Color(0xFFFFE0B2)

val ErrorDark = Color(0xFFFFB4AB)
val OnErrorDark = Color(0xFF690005)
val ErrorContainerDark = Color(0xFF93000A)
val OnErrorContainerDark = Color(0xFFFFDAD6)

@Immutable
data class ExtendedColorScheme(
    val success: Color,
    val onSuccess: Color,
    val successContainer: Color,
    val onSuccessContainer: Color,
    val warning: Color,
    val onWarning: Color,
    val warningContainer: Color,
    val onWarningContainer: Color,
    val error: Color,
    val onError: Color,
    val errorContainer: Color,
    val onErrorContainer: Color
)

internal val LightExtendedColorScheme = ExtendedColorScheme(
    success = SuccessLight,
    onSuccess = OnSuccessLight,
    successContainer = SuccessContainerLight,
    onSuccessContainer = OnSuccessContainerLight,
    warning = WarningLight,
    onWarning = OnWarningLight,
    warningContainer = WarningContainerLight,
    onWarningContainer = OnWarningContainerLight,
    error = ErrorLight,
    onError = OnErrorLight,
    errorContainer = ErrorContainerLight,
    onErrorContainer = OnErrorContainerLight
)

internal val DarkExtendedColorScheme = ExtendedColorScheme(
    success = SuccessDark,
    onSuccess = OnSuccessDark,
    successContainer = SuccessContainerDark,
    onSuccessContainer = OnSuccessContainerDark,
    warning = WarningDark,
    onWarning = OnWarningDark,
    warningContainer = WarningContainerDark,
    onWarningContainer = OnWarningContainerDark,
    error = ErrorDark,
    onError = OnErrorDark,
    errorContainer = ErrorContainerDark,
    onErrorContainer = OnErrorContainerDark
)

internal val DarkColorScheme = darkColorScheme(
    primary = PrimaryDark,
    onPrimary = OnPrimaryDark,
    primaryContainer = PrimaryContainerDark,
    onPrimaryContainer = OnPrimaryContainerDark,
    secondary = SecondaryDark,
    onSecondary = OnSecondaryDark,
    secondaryContainer = SecondaryContainerDark,
    onSecondaryContainer = OnSecondaryContainerDark,
    tertiary = TertiaryDark,
    onTertiary = OnTertiaryDark,
    background = BackgroundDark,
    surface = SurfaceDark,
    onBackground = OnBackgroundDark,
    onSurface = OnSurfaceDark,
    surfaceVariant = SurfaceVariantDark,
    onSurfaceVariant = OnSurfaceVariantDark,
    error = ErrorDark,
    onError = OnErrorDark,
    errorContainer = ErrorContainerDark,
    onErrorContainer = OnErrorContainerDark
)

internal val LightColorScheme = lightColorScheme(
    primary = PrimaryLight,
    onPrimary = OnPrimaryLight,
    primaryContainer = PrimaryContainerLight,
    onPrimaryContainer = OnPrimaryContainerLight,
    secondary = SecondaryLight,
    onSecondary = OnSecondaryLight,
    secondaryContainer = SecondaryContainerLight,
    onSecondaryContainer = OnSecondaryContainerLight,
    tertiary = TertiaryLight,
    onTertiary = OnTertiaryLight,
    background = BackgroundLight,
    surface = SurfaceLight,
    onBackground = OnBackgroundLight,
    onSurface = OnSurfaceLight,
    surfaceVariant = SurfaceVariantLight,
    onSurfaceVariant = OnSurfaceVariantLight,
    error = ErrorLight,
    onError = OnErrorLight,
    errorContainer = ErrorContainerLight,
    onErrorContainer = OnErrorContainerLight
)