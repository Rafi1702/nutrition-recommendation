package com.example.nutritiontracker.ui.components

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.runningFold


data class FieldProperties<T : Any>(
    val validator: FieldValidator<T>,
    val errorMessage: MutableState<String?> = mutableStateOf(null),
    val isDirty: MutableState<Boolean?> = mutableStateOf(null),
    val isRequired: Boolean = false,
    val valueProvider: (() -> T)? = null
)

class FormBuilder {
    val fieldRegistry = mutableStateMapOf<String, FieldProperties<*>>()

    val isValid: Boolean by derivedStateOf {
        fieldRegistry.values.filter { it.isRequired }
            .all { it.errorMessage.value == null && it.isDirty.value == true }
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> getField(key: String): FieldProperties<T>? {
        val properties = fieldRegistry[key] ?: return null

        return properties as FieldProperties<T>?
    }

    fun <T : Any> addField(name: String, field: FieldProperties<T>) {
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
fun rememberFormBuilder(): FormBuilder {
    return remember { FormBuilder() }
}

private data class ValidationState<T>(
    val value: T,
    val error: String?
)

@Composable
fun <T : Any> RegisterFormListener(
    form: FormBuilder,
    name: String,
    fieldProperties: FieldProperties<T>,
    effectKey: Any? = Unit,
) {
    LaunchedEffect(effectKey) {
        form.addField(name, fieldProperties)

        fieldProperties.valueProvider?.let { valueProvider ->
            snapshotFlow {
                val currentValue = valueProvider()

                @Suppress("UNCHECKED_CAST")
                val registeredProps = form.fieldRegistry[name] as? FieldProperties<T>
                val validator = registeredProps?.validator ?: fieldProperties.validator
                ValidationState(currentValue, validator.validate(currentValue))
            }
                .runningFold(
                    Pair(
                        ValidationState(
                            valueProvider(),
                            fieldProperties.validator.validate(valueProvider())
                        ),
                        ValidationState(
                            valueProvider(),
                            fieldProperties.validator.validate(valueProvider())
                        )
                    )
                ) { acc, current ->
                    Pair(acc.second, current)
                }
                .drop(1)
                .collect { (previous, current) ->
                    @Suppress("UNCHECKED_CAST")
                    val currentRegisteredProperties =
                        form.fieldRegistry[name] as? FieldProperties<T>

                    currentRegisteredProperties?.let { properties ->
                        properties.errorMessage.value = current.error
                        properties.isDirty.value = previous.value != current.value

                        Log.d(
                            "[FORM_LISTENER]",
                            "field: $name, previous: ${previous.value}, current: ${current.value}, error: ${current.error}"
                        )
                    }
                }
        }
    }
}


@Composable
fun Form(
    modifier: Modifier = Modifier,
    verticalSpacing: Dp = 8.dp,
    child: @Composable ((isValid: Boolean, form: FormBuilder) -> Unit)? = null
) {
    val builder = rememberFormBuilder()

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

abstract class FieldValidator<T : Any?> {
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

class CheckRequiredValidator : FieldValidator<Boolean>() {
    override fun validate(value: Boolean): String? {
        return if (value) null else "Must be checked"
    }
}

class MatchValidator(
    private val form: FormBuilder,
    private val targetFieldKey: String,
    private val customErrorMessage: String = "Passwords do not match"
) : FieldValidator<CharSequence>() {
    override fun validate(value: CharSequence): String? {
        val targetProps = form.getField<CharSequence>(targetFieldKey)
        val targetValue = targetProps?.valueProvider?.invoke()

        Log.d("[MATCH_VALIDATOR]", "targetValue: $targetValue, currentValue: $value")

        if (value.isBlank()) {
            return "Password confirmation cannot be empty"
        }

        return if (value.toString() != targetValue?.toString()) customErrorMessage else null
    }
}


