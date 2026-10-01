package com.example.nutritiontracker.ui.components

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
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
import com.example.nutritiontracker.ui.theme.LocalForm
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull


data class FieldProperties<T>(
    val validator: List<FieldValidator<T>>,
    val errorMessage: MutableState<String?> = mutableStateOf(null),
    val isDirty: MutableState<Boolean?> = mutableStateOf(null),
    val isRequired: Boolean = false,
    val valueProvider: (() -> T)? = null,
    val transformer: ((T) -> T)? = null
) {
    constructor(
        validator: FieldValidator<T>,
        errorMessage: MutableState<String?> = mutableStateOf(null),
        isDirty: MutableState<Boolean?> = mutableStateOf(null),
        isRequired: Boolean = false,
        valueProvider: (() -> T)? = null,
        transformer: ((T) -> T)? = null
    ) : this(
        validator = listOf(validator),
        errorMessage = errorMessage,
        isDirty = isDirty,
        isRequired = isRequired,
        valueProvider = valueProvider
    )
}

class FormBuilder {
    val fieldRegistry = mutableStateMapOf<String, FieldProperties<*>>()

    val isValid: Boolean by derivedStateOf {
        if (fieldRegistry.isEmpty()) return@derivedStateOf false
        fieldRegistry.values.filter { it.isRequired }
            .all { it.errorMessage.value == null && it.isDirty.value == true }
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> getField(key: String): FieldProperties<T>? {
        val properties = fieldRegistry[key] ?: return null
        return properties as FieldProperties<T>?
    }

    fun <T> addField(name: String, field: FieldProperties<T>) {
        fieldRegistry[name] = field
    }

    fun removeField(name: String) {
        fieldRegistry.remove(name)
    }

    fun clear() {
        fieldRegistry.clear()
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> getValidator(key: String): List<FieldValidator<T>>? {
        val properties = fieldRegistry[key] as? FieldProperties<T>
        return properties?.validator
    }

    fun updateFieldState(
        name: String,
        errorMessage: String?,
        isDirty: Boolean
    ) {
        val properties = fieldRegistry[name] ?: return
        properties.errorMessage.value = errorMessage
        properties.isDirty.value = isDirty
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


/**
 * This function used for registering composable to the FormBuilder
 * @param form is the instance of FormBuilder
 * @param name the registered field name (must be unique for 1 Instance Form Composable)
 * @param fieldProperties the properties of registered fields
 * @param effectKey overriding key for LaunchedEffect
Flow of the Observer (snapshot flow on Launched Effect). The snapshotFlow will observe mutableState in FormBuilder.
there are no constraint to manage when the lambda going to execute. so if one of mutableState on FormBuilder is changing,
it executes the action on snapshotFlow.
Registered field into Form Builder -> find the first error -> accumulating the ValidationState into Pair that contain (previous, current)
-> collected the Pair of ValidationState and then update fieldState value
 * */
@Composable
fun <T> RegisterFormListener(
    form: FormBuilder,
    name: String,
    fieldProperties: FieldProperties<T>,
    effectKey: Any? = Unit,
) {
    LaunchedEffect(effectKey) {
        form.addField(name, fieldProperties)

        val rawInitialValue = fieldProperties.valueProvider?.invoke() ?: return@LaunchedEffect
        // Intercept initial value jika ada transformer
        val initialValue = fieldProperties.transformer?.invoke(rawInitialValue) ?: rawInitialValue

        snapshotFlow { fieldProperties.valueProvider.invoke() }
            .filterNotNull()
            .distinctUntilChanged()
            .collect { rawCurrentValue ->
                val validators = form.getField<T>(name)?.validator ?: fieldProperties.validator

                val currentValue = fieldProperties.transformer?.invoke(rawCurrentValue) ?: rawCurrentValue
                val firstError = validators.firstNotNullOfOrNull { it.validate(currentValue) }

                val isDirty = currentValue != initialValue

                form.updateFieldState(
                    name = name,
                    errorMessage = firstError,
                    isDirty = isDirty
                )

                Log.d(
                    "[FORM_LISTENER]",
                    "field: $name, current: $currentValue, error: $firstError, isDirty: $isDirty"
                )
            }
    }
}

@Composable
fun Form(
    modifier: Modifier = Modifier,
    verticalSpacing: Dp = 8.dp,
    child: @Composable ((isValid: Boolean) -> Unit)? = null
) {
    val builder = rememberFormBuilder()

    DisposableEffect(Unit) {
        onDispose { builder.clear() }
    }

    CompositionLocalProvider(LocalForm provides builder) {
        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(verticalSpacing)
        ) {
            child?.invoke(builder.isValid)
        }
    }
}

@Composable
fun <T> FieldRegister(
    fieldProperties: FieldProperties<T>,
    name: String,
    content: @Composable (() -> Unit)?
) {
    content?.let {
        val form = LocalForm.current
        RegisterFormListener(
            form = form,
            name = name,
            fieldProperties = fieldProperties,
        )

        DisposableEffect(name) {
            onDispose { form.removeField(name) }
        }

        content.invoke()
    }
}

interface FieldValidator<T> {
    fun validate(value: T): String?
}

class EmailValidator : FieldValidator<CharSequence> {
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

class PasswordValidator : FieldValidator<CharSequence> {
    override fun validate(value: CharSequence): String? {
        return when {
            value.isBlank() -> "Password cannot be empty"
            value.length < 8 -> "Password must be at least 8 characters"
            else -> null
        }
    }
}

class CheckRequiredValidator : FieldValidator<Boolean> {
    override fun validate(value: Boolean): String? {
        return if (value) null else "Must be checked"
    }
}

class MockNumberValidator : FieldValidator<Number> {
    override fun validate(value: Number): String? {
        Log.d("[VALIDATOR]", "Number Validator: $value")
        val isGreaterThanZero = when (value) {
            is Int -> value > 0
            is Long -> value > 0L
            is Float -> value > 0f
            is Double -> value > 0.0
            is Short -> value > 0
            is Byte -> value > 0
            else -> value.toDouble() > 0.0
        }

        return if (isGreaterThanZero) null else "Value cannot less than zero"
    }
}

class MatchValidator(
    private val form: FormBuilder,
    private val targetFieldKey: String,
    private val customErrorMessage: String = "Passwords do not match"
) : FieldValidator<CharSequence> {
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
