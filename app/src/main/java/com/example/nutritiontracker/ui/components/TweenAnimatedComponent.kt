package com.example.nutritiontracker.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Immutable
data class AnimatedStateHolder(
    val degrees: Float,
    val onClick: () -> Unit,
)

@Composable
fun rememberAnimatedState(
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
): AnimatedStateHolder {
    val rotation = remember { Animatable(0f) }

    return remember(rotation.value) {
        AnimatedStateHolder(onClick = {
            coroutineScope.launch {
                val target = if (rotation.value <= 0f) 180f else 0f
                rotation.animateTo(target)
            }
        }, degrees = rotation.value)
    }
}