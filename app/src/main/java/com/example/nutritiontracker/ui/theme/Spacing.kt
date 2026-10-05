package com.example.nutritiontracker.ui.theme

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Spacing design tokens.
 * Minimum token sizes: 2.dp and 4.dp, followed by multiples of 8.dp.
 */
object Spacing {
    val none: Dp = 0.dp
    val xxs: Dp = 2.dp      // 2.dp
    val xs: Dp = 4.dp       // 4.dp
    val s: Dp = 8.dp        // 8.dp
    val m: Dp = 16.dp       // 16.dp
    val l: Dp = 24.dp       // 24.dp
    val xl: Dp = 32.dp      // 32.dp
    val xxl: Dp = 40.dp     // 40.dp
    val xxxl: Dp = 48.dp    // 48.dp
    val xxxxl: Dp = 56.dp   // 56.dp
    val xxxxxl: Dp = 64.dp  // 64.dp

    // Named aliases for readability
    val extraExtraSmall: Dp = xxs
    val extraSmall: Dp = xs
    val small: Dp = s
    val medium: Dp = m
    val large: Dp = l
    val extraLarge: Dp = xl
    val extraExtraLarge: Dp = xxl
}
