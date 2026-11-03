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
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import com.example.nutritiontracker.domain.model.UserProfile
import com.example.nutritiontracker.ui.theme.LocalUserProfile
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

sealed class MutationState<out T> {
    data object Idle : MutationState<Nothing>()
    data object Pending : MutationState<Nothing>()
    data class Success<out T>(val data: T) : MutationState<T>()
    data class Error(val error: Throwable) : MutationState<Nothing>()
}

sealed class QueryState<out T>{
    data class Initial<out T>(val initial: T? = null): QueryState<T>()
    data object Loading : QueryState<Nothing>()
    data class Success<out T>(val data: T): QueryState<T>()
    data class Error(val error: Throwable): QueryState<Nothing>()
}

@Composable
internal inline fun <reified VM : ViewModel, reified VMF> AssistedUserViewModel(noinline creationCallback: (VMF, UserProfile?) -> VM): VM {
    val currentUser = LocalUserProfile.current.value
    return hiltViewModel<VM, VMF>(
        creationCallback = { factory -> creationCallback(factory, currentUser) }
    )
}

@Composable
private fun <T> MutationEffect(
    mutationState: MutationState<T>,
    activeWhen: Boolean = true,
    onSuccess: (T) -> Unit,
    onFailure: (Throwable) -> Unit
) {
    if (!activeWhen) return

    LaunchedEffect(mutationState) {
        when (mutationState) {
            is MutationState.Success -> {
                onSuccess(mutationState.data)
            }

            is MutationState.Error -> {
                onFailure(mutationState.error)
            }

            else -> {}
        }
    }
}

