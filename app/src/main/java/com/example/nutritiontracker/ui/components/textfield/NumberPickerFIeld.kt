package com.example.nutritiontracker.ui.components.textfield

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.nutritiontracker.ui.components.FieldProperties
import com.example.nutritiontracker.ui.components.FieldRegister
import com.example.nutritiontracker.ui.components.Form
import com.example.nutritiontracker.ui.components.MockNumberValidator
import com.example.nutritiontracker.ui.theme.LocalForm
import com.example.nutritiontracker.ui.theme.NutritionTrackerTheme
import com.example.nutritiontracker.ui.theme.Spacing
import com.example.nutritiontracker.ui.theme.colorScheme
import com.example.nutritiontracker.ui.theme.typography


class NumberPickerState<T>(
    initialValue: T,
    private val onIncrement: (T) -> T,
    private val onDecrement: (T) -> T
) {
    val valueState: MutableState<T> = mutableStateOf(initialValue)

    val value: T
        get() = valueState.value

    fun increment() {
        valueState.value = onIncrement(valueState.value)
    }

    fun decrement() {
        valueState.value = onDecrement(valueState.value)
    }
}

@Composable
fun rememberIntPickerState(
    initialValue: Int = 0,
    step: Int = 1
): NumberPickerState<Int> = remember(initialValue, step) {
    NumberPickerState(
        initialValue = initialValue,
        onIncrement = { current -> current + step },
        onDecrement = { current -> current - step }
    )
}

@Composable
fun rememberDoublePickerState(
    initialValue: Double = 0.0,
    step: Double = 1.0
): NumberPickerState<Double> = remember(initialValue, step) {
    NumberPickerState(
        initialValue = initialValue,
        onIncrement = { current -> current + step },
        onDecrement = { current -> current - step }
    )
}

@Composable
fun rememberFloatPickerState(
    initialValue: Float = 0f,
    step: Float = 1f
): NumberPickerState<Float> = remember(initialValue, step) {
    NumberPickerState(
        initialValue = initialValue,
        onIncrement = { current -> current + step },
        onDecrement = { current -> current - step }
    )
}

@Composable
fun rememberLongPickerState(
    initialValue: Long = 0L,
    step: Long = 1L
): NumberPickerState<Long> = remember(initialValue, step) {
    NumberPickerState(
        initialValue = initialValue,
        onIncrement = { current -> current + step },
        onDecrement = { current -> current - step }
    )
}

@Composable
fun <T> IncrementerAndDecrementerAction(
    modifier: Modifier = Modifier,
    state: NumberPickerState<T>,
    isIncrementEnabled: Boolean = true,
    isDecrementEnabled: Boolean = true,
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(Spacing.s),
        color = colorScheme.surface,
        contentColor = colorScheme.onSurface,
        border = BorderStroke(1.dp, colorScheme.outlineVariant.copy(alpha = 0.5f))
    ) {
        Column(
            modifier = Modifier
                .width(IntrinsicSize.Min)
                .padding(vertical = Spacing.xxs, horizontal = Spacing.xs),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(
                Icons.Default.KeyboardArrowUp,
                modifier = Modifier.clickable(enabled = isIncrementEnabled) {
                    if (isIncrementEnabled) state.increment()
                },
                contentDescription = "number_field_value_incrementer",
                tint = colorScheme.onSurface.copy(
                    alpha = if (isIncrementEnabled) 1f else 0.38f
                )
            )
            HorizontalDivider(
                modifier = Modifier.width(Spacing.m),
                color = colorScheme.outlineVariant.copy(alpha = 0.5f)
            )
            Icon(
                Icons.Default.KeyboardArrowDown,
                modifier = Modifier.clickable(enabled = isDecrementEnabled) {
                    if (isDecrementEnabled) state.decrement()
                },
                contentDescription = "number_field_value_decrementer",
                tint = colorScheme.onSurface.copy(
                    alpha = if (isDecrementEnabled) 1f else 0.38f
                )
            )
        }
    }
}

@Composable
private fun <T> NumberPickerFieldContent(
    modifier: Modifier = Modifier,
    fieldProperties: FieldProperties<T>,
    state: NumberPickerState<T>,
    fieldName: String,
    label: String,
    unit: String? = null,
    keyboardType: KeyboardType = KeyboardType.Number,
    parseValue: (String) -> T,
    isIncrementEnabled: Boolean = true,
    isDecrementEnabled: Boolean = true,
) {
    var textInput by remember { mutableStateOf(state.value.toString()) }

    LaunchedEffect(state.value) {
        if (parseValue(textInput) != state.value || (textInput.isBlank() && state.value != parseValue(""))) {
            textInput = state.value.toString()
        }
    }

    FieldRegister(
        fieldProperties = fieldProperties.copy(
            valueState = state.valueState
        ),
        name = fieldName,
    ) {
        val registeredProps = LocalForm.current.getField<T>(fieldName) ?: fieldProperties
        val currentValue = state.value
        val hasError = registeredProps.errorMessage.value != null
        val isValidValue = registeredProps.validator.none { it.validate(currentValue) != null }
        val canDecrement = isDecrementEnabled && !hasError && isValidValue

        TextFormField(
            modifier = modifier
                .widthIn(max = 160.dp)
                .heightIn(max = 120.dp),
            value = textInput,
            onValueChange = { newText ->
                val filteredText = newText.filter { char ->
                    char.isDigit() || (keyboardType == KeyboardType.Decimal && char == '.')
                }
                textInput = filteredText
                state.valueState.value = parseValue(filteredText)
            },
            keyboardOption = KeyboardOptions(keyboardType = keyboardType),
            textStyle = typography.headlineLarge.copy(
                color = colorScheme.onSurface,
            ),
            decorationBox = { innerTextField ->
                Surface(shape = RoundedCornerShape(Spacing.s)) {
                    Column(
                        modifier = Modifier.padding(vertical = Spacing.s, horizontal = Spacing.m),
                        verticalArrangement = Arrangement.spacedBy(Spacing.xxs)
                    ) {
                        Text(label)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(IntrinsicSize.Min),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(modifier = Modifier.weight(2f)) {
                                Box(
                                    modifier = Modifier
                                        .weight(2f)
                                        .fillMaxHeight(),
                                    contentAlignment = Alignment.CenterStart
                                ) {
                                    innerTextField()
                                }

                                unit?.let {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxHeight()
                                            .weight(1f),
                                        contentAlignment = Alignment.CenterStart
                                    ) {
                                        Text(unit)
                                    }
                                }
                            }
                            IncrementerAndDecrementerAction(
                                modifier = Modifier.weight(1f),
                                state = state,
                                isIncrementEnabled = isIncrementEnabled,
                                isDecrementEnabled = canDecrement
                            )
                        }
                    }
                }
            },
        )
    }
}

// --- Int Overloads ---

@JvmName("NumberPickerFieldIntState")
@Composable
fun NumberPickerField(
    modifier: Modifier = Modifier,
    fieldProperties: FieldProperties<Int>,
    state: NumberPickerState<Int>,
    fieldName: String,
    label: String,
    unit: String? = null,
    isIncrementEnabled: Boolean = true,
    isDecrementEnabled: Boolean = true,
) {
    NumberPickerFieldContent(
        modifier = modifier,
        fieldProperties = fieldProperties,
        state = state,
        fieldName = fieldName,
        label = label,
        unit = unit,
        keyboardType = KeyboardType.Number,
        parseValue = { text -> text.toIntOrNull() ?: 0 },
        isIncrementEnabled = isIncrementEnabled,
        isDecrementEnabled = isDecrementEnabled
    )
}

@JvmName("NumberPickerFieldInt")
@Composable
fun NumberPickerField(
    modifier: Modifier = Modifier,
    fieldProperties: FieldProperties<Int>,
    initialValue: Int,
    fieldName: String,
    label: String,
    step: Int = 1,
    unit: String? = null,
) {
    LaunchedEffect(fieldProperties.isValid, fieldProperties.isDirty) {
        Log.d(
            "[NUMBER_PICKER_FIELD]",
            "field valid: ${fieldProperties.isValid}, errorMessage: ${fieldProperties.errorMessage.value}, dirty: ${fieldProperties.isDirty.value}"
        )
    }

    val state = rememberIntPickerState(initialValue = initialValue, step = step)

    NumberPickerField(
        modifier = modifier,
        fieldProperties = fieldProperties,
        state = state,
        fieldName = fieldName,
        label = label,
        unit = unit,
        isDecrementEnabled = fieldProperties.isValid || fieldProperties.errorMessage.value == null
    )
}

@JvmName("NumberPickerFieldDoubleState")
@Composable
fun NumberPickerField(
    modifier: Modifier = Modifier,
    fieldProperties: FieldProperties<Double>,
    state: NumberPickerState<Double>,
    fieldName: String,
    label: String,
    unit: String? = null,
    isIncrementEnabled: Boolean = true,
    isDecrementEnabled: Boolean = true,
) {
    NumberPickerFieldContent(
        modifier = modifier,
        fieldProperties = fieldProperties,
        state = state,
        fieldName = fieldName,
        label = label,
        unit = unit,
        keyboardType = KeyboardType.Decimal,
        parseValue = { text -> text.toDoubleOrNull() ?: 0.0 },
        isIncrementEnabled = isIncrementEnabled,
        isDecrementEnabled = isDecrementEnabled
    )
}

@JvmName("NumberPickerFieldDouble")
@Composable
fun NumberPickerField(
    modifier: Modifier = Modifier,
    fieldProperties: FieldProperties<Double>,
    initialValue: Double,
    fieldName: String,
    label: String,
    step: Double = 1.0,
    unit: String? = null,
) {
    val state = rememberDoublePickerState(initialValue = initialValue, step = step)

    NumberPickerField(
        modifier = modifier,
        fieldProperties = fieldProperties,
        state = state,
        fieldName = fieldName,
        label = label,
        unit = unit,
        isDecrementEnabled = fieldProperties.isValid || fieldProperties.errorMessage.value == null
    )
}

// --- Float Overloads ---

@JvmName("NumberPickerFieldFloatState")
@Composable
fun NumberPickerField(
    modifier: Modifier = Modifier,
    fieldProperties: FieldProperties<Float>,
    state: NumberPickerState<Float>,
    fieldName: String,
    label: String,
    unit: String? = null,
    isIncrementEnabled: Boolean = true,
    isDecrementEnabled: Boolean = true,
) {
    NumberPickerFieldContent(
        modifier = modifier,
        fieldProperties = fieldProperties,
        state = state,
        fieldName = fieldName,
        label = label,
        unit = unit,
        keyboardType = KeyboardType.Decimal,
        parseValue = { text -> text.toFloatOrNull() ?: 0f },
        isIncrementEnabled = isIncrementEnabled,
        isDecrementEnabled = isDecrementEnabled
    )
}

@JvmName("NumberPickerFieldFloat")
@Composable
fun NumberPickerField(
    modifier: Modifier = Modifier,
    fieldProperties: FieldProperties<Float>,
    initialValue: Float,
    fieldName: String,
    label: String,
    step: Float = 1f,
    unit: String? = null,
) {
    val state = rememberFloatPickerState(initialValue = initialValue, step = step)

    NumberPickerField(
        modifier = modifier,
        fieldProperties = fieldProperties,
        state = state,
        fieldName = fieldName,
        label = label,
        unit = unit,
        isDecrementEnabled = fieldProperties.isValid || fieldProperties.errorMessage.value == null
    )
}

// --- Long Overloads ---

@JvmName("NumberPickerFieldLongState")
@Composable
fun NumberPickerField(
    modifier: Modifier = Modifier,
    fieldProperties: FieldProperties<Long>,
    state: NumberPickerState<Long>,
    fieldName: String,
    label: String,
    unit: String? = null,
    isIncrementEnabled: Boolean = true,
    isDecrementEnabled: Boolean = true,
) {
    NumberPickerFieldContent(
        modifier = modifier,
        fieldProperties = fieldProperties,
        state = state,
        fieldName = fieldName,
        label = label,
        unit = unit,
        keyboardType = KeyboardType.Number,
        parseValue = { text -> text.toLongOrNull() ?: 0L },
        isIncrementEnabled = isIncrementEnabled,
        isDecrementEnabled = isDecrementEnabled
    )
}

@JvmName("NumberPickerFieldLong")
@Composable
fun NumberPickerField(
    modifier: Modifier = Modifier,
    fieldProperties: FieldProperties<Long>,
    initialValue: Long,
    fieldName: String,
    label: String,
    step: Long = 1L,
    unit: String? = null,
) {
    val state = rememberLongPickerState(initialValue = initialValue, step = step)

    NumberPickerField(
        modifier = modifier,
        fieldProperties = fieldProperties,
        state = state,
        fieldName = fieldName,
        label = label,
        unit = unit,
        isDecrementEnabled = fieldProperties.isValid || fieldProperties.errorMessage.value == null
    )
}

// --- Generic Number Overload ---

@JvmName("NumberPickerFieldGenericNumber")
@Composable
fun NumberPickerField(
    modifier: Modifier = Modifier,
    fieldProperties: FieldProperties<Number>,
    initialValue: Number,
    fieldName: String,
    label: String,
    unit: String? = null,
) {
    when (initialValue) {
        is Double -> NumberPickerField(
            modifier = modifier,
            fieldProperties = @Suppress("UNCHECKED_CAST") (fieldProperties as FieldProperties<Double>),
            initialValue = initialValue,
            fieldName = fieldName,
            label = label,
            unit = unit
        )
        is Float -> NumberPickerField(
            modifier = modifier,
            fieldProperties = @Suppress("UNCHECKED_CAST") (fieldProperties as FieldProperties<Float>),
            initialValue = initialValue,
            fieldName = fieldName,
            label = label,
            unit = unit
        )
        is Long -> NumberPickerField(
            modifier = modifier,
            fieldProperties = @Suppress("UNCHECKED_CAST") (fieldProperties as FieldProperties<Long>),
            initialValue = initialValue,
            fieldName = fieldName,
            label = label,
            unit = unit
        )
        else -> NumberPickerField(
            modifier = modifier,
            fieldProperties = @Suppress("UNCHECKED_CAST") (fieldProperties as FieldProperties<Int>),
            initialValue = initialValue.toInt(),
            fieldName = fieldName,
            label = label,
            unit = unit
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun NumberPickerFieldPreview() {
    NutritionTrackerTheme {
        Form {
            NumberPickerField(
                fieldProperties = FieldProperties(
                    validator = MockNumberValidator(),
                    isRequired = true
                ),
                initialValue = 0,
                fieldName = "Age",
                unit = "CM",
                label = "SOMETHING"
            )
        }
    }
}
