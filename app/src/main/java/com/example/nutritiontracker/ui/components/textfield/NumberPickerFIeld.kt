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
import androidx.compose.ui.unit.dp
import com.example.nutritiontracker.ui.components.FieldProperties
import com.example.nutritiontracker.ui.components.FieldRegister
import com.example.nutritiontracker.ui.components.currentForm
import com.example.nutritiontracker.ui.theme.Spacing
import com.example.nutritiontracker.ui.theme.colorScheme
import com.example.nutritiontracker.ui.theme.typography
import kotlin.reflect.KProperty1


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
private fun <M : Any, T> NumberPickerFieldContent(
    modifier: Modifier = Modifier,
    fieldProperties: FieldProperties<T>? = null,
    state: NumberPickerState<T>,
    fieldName: KProperty1<M, T>,
    label: String,
    unit: String? = null,
    keyboardType: KeyboardType = KeyboardType.Number,
    parseValue: (String) -> T,
    isIncrementEnabled: Boolean = true,
    isDecrementEnabled: Boolean = true,
) {
    val formBuilder = currentForm<M>()
    val existingField = formBuilder.getField<T>(fieldName) ?: FieldProperties(validator = listOf())

    var textInput by remember { mutableStateOf(state.value.toString()) }

    LaunchedEffect(state.value) {
        if (parseValue(textInput) != state.value || (textInput.isBlank() && state.value != parseValue(""))) {
            textInput = state.value.toString()
        }
    }


    FieldRegister(
        fieldProperties = existingField,
        name = fieldName,
    ) {

        val currentValue = state.value
        val hasError = existingField.errorMessage.value != null
        val isValidValue = existingField.validator.none { it.validate(currentValue) != null }
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
            textStyle = typography.displaySmall.copy(
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
fun <M : Any> NumberPickerField(
    modifier: Modifier = Modifier,
    fieldProperties: FieldProperties<Int>? = null,
    state: NumberPickerState<Int>,
    fieldName: KProperty1<M, Int>,
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
fun <M : Any> NumberPickerField(
    modifier: Modifier = Modifier,
    fieldProperties: FieldProperties<Int>? = null,
    initialValue: Int = 0,
    fieldName: KProperty1<M, Int>,
    label: String,
    step: Int = 1,
    unit: String? = null,
) {
    val formBuilder = currentForm<M>()
    val existingField = formBuilder.getField<Int>(fieldName)
    val startValue = existingField?.state?.value ?: initialValue

    val props = existingField ?: fieldProperties
    LaunchedEffect(props?.isValid, props?.isDirty) {
        Log.d(
            "[NUMBER_PICKER_FIELD]",
            "field valid: ${props?.isValid}, errorMessage: ${props?.errorMessage?.value}, dirty: ${props?.isDirty}"
        )
    }

    val state = rememberIntPickerState(initialValue = startValue, step = step)
    val isDecEnabled = props?.let { it.isValid || it.errorMessage.value == null } ?: true

    NumberPickerField(
        modifier = modifier,
        fieldProperties = fieldProperties,
        state = state,
        fieldName = fieldName,
        label = label,
        unit = unit,
        isDecrementEnabled = isDecEnabled
    )
}

// --- Double Overloads ---

@JvmName("NumberPickerFieldDoubleState")
@Composable
fun <M : Any> NumberPickerField(
    modifier: Modifier = Modifier,
    fieldProperties: FieldProperties<Double>? = null,
    state: NumberPickerState<Double>,
    fieldName: KProperty1<M, Double>,
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
fun <M : Any> NumberPickerField(
    modifier: Modifier = Modifier,
    fieldProperties: FieldProperties<Double>? = null,
    initialValue: Double = 0.0,
    fieldName: KProperty1<M, Double>,
    label: String,
    step: Double = 1.0,
    unit: String? = null,
) {
    val formBuilder = currentForm<M>()
    val existingField = formBuilder.getField<Double>(fieldName)
    val startValue = existingField?.state?.value ?: initialValue

    val state = rememberDoublePickerState(initialValue = startValue, step = step)
    val props = existingField ?: fieldProperties
    val isDecEnabled = props?.let { it.isValid || it.errorMessage.value == null } ?: true

    NumberPickerField(
        modifier = modifier,
        fieldProperties = fieldProperties,
        state = state,
        fieldName = fieldName,
        label = label,
        unit = unit,
        isDecrementEnabled = isDecEnabled
    )
}

// --- Float Overloads ---

@JvmName("NumberPickerFieldFloatState")
@Composable
fun <M : Any> NumberPickerField(
    modifier: Modifier = Modifier,
    fieldProperties: FieldProperties<Float>? = null,
    state: NumberPickerState<Float>,
    fieldName: KProperty1<M, Float>,
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
fun <M : Any> NumberPickerField(
    modifier: Modifier = Modifier,
    fieldProperties: FieldProperties<Float>? = null,
    initialValue: Float = 0f,
    fieldName: KProperty1<M, Float>,
    label: String,
    step: Float = 1f,
    unit: String? = null,
) {
    val formBuilder = currentForm<M>()
    val existingField = formBuilder.getField<Float>(fieldName)
    val startValue = existingField?.state?.value ?: initialValue

    val state = rememberFloatPickerState(initialValue = startValue, step = step)
    val props = existingField ?: fieldProperties
    val isDecEnabled = props?.let { it.isValid || it.errorMessage.value == null } ?: true

    NumberPickerField(
        modifier = modifier,
        fieldProperties = fieldProperties,
        state = state,
        fieldName = fieldName,
        label = label,
        unit = unit,
        isDecrementEnabled = isDecEnabled
    )
}

// --- Long Overloads ---

@JvmName("NumberPickerFieldLongState")
@Composable
fun <M : Any> NumberPickerField(
    modifier: Modifier = Modifier,
    fieldProperties: FieldProperties<Long>? = null,
    state: NumberPickerState<Long>,
    fieldName: KProperty1<M, Long>,
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
fun <M : Any> NumberPickerField(
    modifier: Modifier = Modifier,
    fieldProperties: FieldProperties<Long>? = null,
    initialValue: Long = 0L,
    fieldName: KProperty1<M, Long>,
    label: String,
    step: Long = 1L,
    unit: String? = null,
) {
    val formBuilder = currentForm<M>()
    val existingField = formBuilder.getField<Long>(fieldName)
    val startValue = existingField?.state?.value ?: initialValue

    val state = rememberLongPickerState(initialValue = startValue, step = step)
    val props = existingField ?: fieldProperties
    val isDecEnabled = props?.let { it.isValid || it.errorMessage.value == null } ?: true

    NumberPickerField(
        modifier = modifier,
        fieldProperties = fieldProperties,
        state = state,
        fieldName = fieldName,
        label = label,
        unit = unit,
        isDecrementEnabled = isDecEnabled
    )
}

data class TestNumberField(
    val age: Double,
)

//@Preview(showBackground = true)
//@Composable
//private fun NumberPickerFieldPreview() {
//    NutritionTrackerTheme {
//        Form<TestNumberField> {
//            NumberPickerField(
//                fieldProperties = FieldProperties(
//                    validator = MockNumberValidator(),
//                    isRequired = true
//                ),
//                initialValue = 0,
//                fieldName = TestNumberField::age,
//                unit = "CM",
//                label = "SOMETHING"
//            )
//        }
//    }
//}
