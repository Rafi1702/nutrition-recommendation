package com.example.nutritiontracker.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

val PrimaryLight = Color(0xFF2E7D32)     // Hijau Tua yang segar
val OnPrimaryLight = Color(0xFFFFFFFF)
val PrimaryContainerLight = Color(0xFFA5D6A7)
val OnPrimaryContainerLight = Color(0xFF002106)

val SecondaryLight = Color(0xFFE65100)   // Oranye Energi (untuk kalori/target)
val OnSecondaryLight = Color(0xFFFFFFFF)
val SecondaryContainerLight = Color(0xFFFFCCBC)
val OnSecondaryContainerLight = Color(0xFF351000)

val TertiaryLight = Color(0xFF00695C)    // Teal untuk hidrasi/protein
val OnTertiaryLight = Color(0xFFFFFFFF)

val BackgroundLight = Color(0xFFF8FBF8)  // Putih dengan sedikit sentuhan hijau sangat muda
val SurfaceLight = Color(0xFFFFFFFF)
val OnBackgroundLight = Color(0xFF191C19)
val OnSurfaceLight = Color(0xFF191C19)

// --- Dark Theme Colors ---
val PrimaryDark = Color(0xFF81C784)      // Hijau Mint lembut untuk dark mode
val OnPrimaryDark = Color(0xFF003910)
val PrimaryContainerDark = Color(0xFF00531C)
val OnPrimaryContainerDark = Color(0xFFA5D6A7)

val SecondaryDark = Color(0xFFFFAB91)    // Oranye lembut
val OnSecondaryDark = Color(0xFF5C1A00)
val SecondaryContainerDark = Color(0xFF832A00)
val OnSecondaryContainerDark = Color(0xFFFFCCBC)

val TertiaryDark = Color(0xFF4DB6AC)     // Teal lembut
val OnTertiaryDark = Color(0xFF003732)

val BackgroundDark = Color(0xFF111411)   // Hitam keabuan dengan nuansa natural
val SurfaceDark = Color(0xFF111411)
val OnBackgroundDark = Color(0xFFE2E3DF)
val OnSurfaceDark = Color(0xFFE2E3DF)


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
    onSurface = OnSurfaceDark
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
    onSurface = OnSurfaceLight
)