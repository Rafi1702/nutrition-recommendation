package com.example.nutritiontracker.ui.components

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

typealias FieldPair<T> = Pair<String, FieldProperties<T>>

data class FieldProperties<T>(
    val validator: FieldValidator<T>,
    val errorMessage: MutableState<String?> = mutableStateOf(null),
    val isDirty: MutableState<Boolean?> = mutableStateOf(null),
    val isRequired: Boolean = false,
)

class FormBuilder<T> {
    val fieldRegistry = mutableStateMapOf<String, FieldProperties<T>>()

    val isValid: Boolean by derivedStateOf {
        fieldRegistry.values.filter { it.isRequired }
            .all { it.errorMessage.value == null && it.isDirty.value == true }
    }

    fun addField(name: String, field: FieldProperties<T>) {
        fieldRegistry[name] = field
    }

    fun removeField(name: String) {
        fieldRegistry.remove(name)
    }

    fun clear() {
        fieldRegistry.clear()
    }
}

@Composable
fun <T> rememberFormBuilder(): FormBuilder<T> {
    return remember { FormBuilder() }
}


@Composable
fun <T> Form(
    modifier: Modifier = Modifier,
    verticalSpacing: Dp = 8.dp,
    child: @Composable ((isValid: Boolean, form: FormBuilder<T>) -> Unit)? = null
) {
    val builder = rememberFormBuilder<T>()

    DisposableEffect(Unit) {
        onDispose { builder.clear() }
    }

    return Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(verticalSpacing)
    ) {
        child?.invoke(builder.isValid, builder)
    }
}

abstract class FieldValidator<in T> {
    abstract fun validate(value: T): String?
}

class EmailValidator : FieldValidator<CharSequence>() {
    override fun validate(value: CharSequence): String? {
        Log.d("[DEBUG]", "EMAIL VALIDATOR $value")
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[a-z|A-Z]{2,}$".toRegex()
        return when {
            value.isBlank() -> "Email cannot be empty"
            !value.matches(emailRegex) -> "Invalid email format"
            else -> null
        }
    }
}

class PasswordValidator : FieldValidator<CharSequence>() {
    override fun validate(value: CharSequence): String? {
        return when {
            value.isBlank() -> "Password cannot be empty"
            value.length < 8 -> "Password must be at least 8 characters"
            else -> null
        }
    }
}


