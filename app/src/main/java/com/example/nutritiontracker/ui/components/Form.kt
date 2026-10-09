package com.example.nutritiontracker.ui.components

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.example.nutritiontracker.ui.theme.Spacing
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlin.reflect.KProperty1


data class FieldProperties<T>(
    val validator: List<FieldValidator<T>>,
    val errorMessage: MutableState<String?> = mutableStateOf(null),
    val isDirty: MutableState<Boolean?> = mutableStateOf(null),
    val isRequired: Boolean = false,
    val valueState: State<T>? = null,
) {
    constructor(
        validator: FieldValidator<T>,
        errorMessage: MutableState<String?> = mutableStateOf(null),
        isDirty: MutableState<Boolean?> = mutableStateOf(null),
        isRequired: Boolean = false,
        valueState: State<T>? = null,
    ) : this(
        validator = listOf(validator),
        errorMessage = errorMessage,
        isDirty = isDirty,
        isRequired = isRequired,
        valueState = valueState,
    )


    val isValid: Boolean by derivedStateOf { errorMessage.value == null && isDirty.value == true }
}

class FormBuilder<T : Any> {
    val fieldRegistry = mutableStateMapOf<KProperty1<T, *>, FieldProperties<*>>()

    val isValid: Boolean by derivedStateOf {
        if (fieldRegistry.isEmpty()) return@derivedStateOf false
        fieldRegistry.values.filter { it.valueState != null }
            .all { it.isValid }
    }

    @Suppress("UNCHECKED_CAST")
    fun <V> getField(key: KProperty1<T, *>): FieldProperties<V>? {
        val properties = fieldRegistry[key] ?: return null
        return properties as FieldProperties<V>?
    }

    fun <V> addField(name: KProperty1<T, *>, field: FieldProperties<V>): FormBuilder<T> {
        fieldRegistry[name] = field
        return this
    }

    fun addField(fields: Map<KProperty1<T, *>, FieldProperties<*>>) {
        fieldRegistry.putAll(fields)
    }

    fun removeField(name: KProperty1<T, *>) {
        fieldRegistry.remove(name)
    }

    fun clear() {
        fieldRegistry.clear()
    }

    @Suppress("UNCHECKED_CAST")
    fun getValidator(key: KProperty1<T, *>): List<FieldValidator<T>>? {
        val properties = fieldRegistry[key] as? FieldProperties<T>
        return properties?.validator
    }

    fun updateFieldState(
        name: KProperty1<T, *>,
        errorMessage: String?,
        isDirty: Boolean
    ) {
        val properties = fieldRegistry[name] ?: return
        properties.errorMessage.value = errorMessage
        properties.isDirty.value = isDirty
    }
}

@Composable
fun <T : Any> rememberFormBuilder(): FormBuilder<T> {
    return remember { FormBuilder() }
}

val LocalForm = compositionLocalOf<FormBuilder<*>> {
    error("No FormBuilder provided. Make sure to wrap your form fields inside a Form { ... } composable.")
}

@Suppress("UNCHECKED_CAST")
@Composable
fun <T : Any> currentForm(): FormBuilder<T> {
    return LocalForm.current as FormBuilder<T>
}

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
fun <T : Any> Form(
    modifier: Modifier = Modifier,
    verticalSpacing: Dp = Spacing.s,
    isPersist: Boolean =  false,
    formBuilder: FormBuilder<T>? = null,
    child: @Composable ((isValid: Boolean) -> Unit)? = null,
) {
    val builder = remember(formBuilder) { formBuilder ?: FormBuilder() }

    CompositionLocalProvider(LocalForm provides builder) {
        DisposableEffect(builder) {
            onDispose { if (!isPersist) builder.clear() }
        }

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
fun <T : Any, V> FieldRegister(
    fieldProperties: FieldProperties<V>,
    name: KProperty1<T, *>,
    effectKey: Any? = Unit,
    persist: Boolean = false,
    content: @Composable (() -> Unit)?
) {
    val form = currentForm<T>()

    DisposableEffect(form, name) {
        form.addField(name, fieldProperties)
        onDispose { if(!persist) form.removeField(name) }
    }

    LaunchedEffect(effectKey) {
        val rawInitialValue = fieldProperties.valueState?.value ?: return@LaunchedEffect

        Log.d("[REGISTER_FORM_LISTENER]", "initial value: $rawInitialValue")

        snapshotFlow { fieldProperties.valueState.value }
            .distinctUntilChanged()
            .collect { rawCurrentValue ->
                if (rawCurrentValue == null) return@collect
                val currentProps = form.getField<V>(name) ?: fieldProperties
                val validators = currentProps.validator
                val firstError =
                    validators.firstNotNullOfOrNull { it.validate(rawCurrentValue) }

                val isDirty =
                    currentProps.isDirty.value == true || rawCurrentValue != rawInitialValue

                form.updateFieldState(
                    name = name,
                    errorMessage = firstError,
                    isDirty = isDirty
                )

                Log.d(
                    "[FORM_LISTENER]",
                    "field: $name, current: $rawCurrentValue, error: $firstError, isDirty: $isDirty"
                )
            }
    }

    content?.invoke()
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

class CannotEmptyValidator : FieldValidator<String> {
    override fun validate(value: String): String? {
        return if (value.isNotBlank()) null else "Value Cannot Empty"
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

class MockNumberValidator(minimum: Number = 0, maximum: Number = 0) : FieldValidator<Int> {
    override fun validate(value: Int): String? {
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

class MatchValidator<T: Any>(
    private val form: FormBuilder<T>,
    private val targetFieldKey: KProperty1<T, *>,
    private val customErrorMessage: String = "Passwords do not match"
) : FieldValidator<CharSequence> {
    override fun validate(value: CharSequence): String? {
        val targetProps = form.getField<CharSequence>(targetFieldKey)
        val targetValue = targetProps?.valueState?.value

        Log.d("[MATCH_VALIDATOR]", "targetValue: $targetValue, currentValue: $value")

        if (value.isBlank()) {
            return "Password confirmation cannot be empty"
        }

        return if (value.toString() != targetValue?.toString()) customErrorMessage else null
    }
}
