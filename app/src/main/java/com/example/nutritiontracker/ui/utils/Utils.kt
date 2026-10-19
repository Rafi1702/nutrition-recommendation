package com.example.nutritiontracker.ui.utils

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import kotlinx.coroutines.delay

fun AnnotatedString.Builder.withStyle(
    style: TextStyle,
    color: Color,
    block: AnnotatedString.Builder.() -> Unit,
) {
    withStyle(
        style = SpanStyle(
            color = color,
            fontSize = style.fontSize,
            fontWeight = style.fontWeight,
            fontStyle = style.fontStyle,
            fontFamily = style.fontFamily,
            letterSpacing = style.letterSpacing,
        ), block = block
    )
}


@Composable
fun Debounce(query: String, delayInMillis: Long = 300L, callback: suspend (String) -> Unit) {
    val currentCallback by rememberUpdatedState(callback)

    LaunchedEffect(query) {
        if (query.isNotEmpty()) {
            delay(delayInMillis)
            currentCallback(query)
        }
    }
}

operator fun PaddingValues.plus(other: PaddingValues): PaddingValues = object : PaddingValues {
    override fun calculateLeftPadding(layoutDirection: LayoutDirection): Dp {
        return this@plus.calculateLeftPadding(layoutDirection) + other.calculateLeftPadding(layoutDirection)
    }

    override fun calculateTopPadding(): Dp {
        return this@plus.calculateTopPadding() + other.calculateTopPadding()
    }

    override fun calculateRightPadding(layoutDirection: LayoutDirection): Dp {
        return this@plus.calculateRightPadding(layoutDirection) + other.calculateRightPadding(layoutDirection)
    }

    override fun calculateBottomPadding(): Dp {
        return this@plus.calculateBottomPadding() + other.calculateBottomPadding()
    }
}