package com.example.nutritiontracker.ui.components.textfield

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.nutritiontracker.ui.components.FieldProperties
import com.example.nutritiontracker.ui.components.FieldRegister
import com.example.nutritiontracker.ui.components.Form
import com.example.nutritiontracker.ui.components.MockNumberValidator
import com.example.nutritiontracker.ui.theme.LocalForm
import com.example.nutritiontracker.ui.theme.NutritionTrackerTheme
import com.example.nutritiontracker.ui.theme.colorScheme
import com.example.nutritiontracker.ui.theme.typography


class NumberCreationState<T : Number>(
    initialValue: T,
    private val onIncrement: (T) -> T,
    private val onDecrement: (T) -> T
) {
    val numberState = mutableStateOf(initialValue)

    fun incrementer() {
        numberState.value = onIncrement(numberState.value)
    }

    fun decrementer() {
        numberState.value = onDecrement(numberState.value)
    }

    companion object {
        inline fun <reified T : Number> create(
            initialValue: T,
            adder: T,
            decrementer: T
        ): NumberCreationState<T> {
            val clazz = when (T::class) {
                Number::class -> initialValue::class
                else -> T::class
            }

            val incrementLogic: (T) -> T = when (clazz) {
                Int::class -> { current -> (current.toInt() + adder.toInt()) as T }
                Double::class -> { current -> (current.toDouble() + adder.toDouble()) as T }
                Float::class -> { current -> (current.toFloat() + adder.toFloat()) as T }
                Long::class -> { current -> (current.toLong() + adder.toLong()) as T }
                else -> throw IllegalArgumentException("Unsupported type: $clazz")
            }

            val decrementLogic: (T) -> T = when (clazz) {
                Int::class -> { current -> (current.toInt() - decrementer.toInt()) as T }
                Double::class -> { current -> (current.toDouble() - decrementer.toDouble()) as T }
                Float::class -> { current -> (current.toFloat() - decrementer.toFloat()) as T }
                Long::class -> { current -> (current.toLong() - decrementer.toLong()) as T }
                else -> throw IllegalArgumentException("Unsupported type: $clazz")
            }

            return NumberCreationState(initialValue, incrementLogic, decrementLogic)
        }
    }
}

inline fun <reified T : Number> defaultStep(initialValue: T): T {
    val clazz = when (T::class) {
        Number::class -> initialValue::class
        else -> T::class
    }
    return when (clazz) {
        Int::class -> 1 as T
        Double::class -> 1.0 as T
        Float::class -> 1f as T
        Long::class -> 1L as T
        else -> throw IllegalArgumentException("Unsupported type: $clazz")
    }
}

@Composable
inline fun <reified T : Number> rememberNumberCreationState(
    initialValue: T,
    adder: T = defaultStep(initialValue),
    decrementer: T = defaultStep(initialValue)
): NumberCreationState<T> {
    return remember(initialValue, adder, decrementer) {
        NumberCreationState.create(initialValue, adder, decrementer)
    }
}

@Composable
fun <T : Number> IncrementerAndDecrementerAction(
    modifier: Modifier = Modifier,
    state: NumberCreationState<T>,
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = colorScheme.onSurface,
        contentColor = colorScheme.inverseOnSurface
    ) {
        Column(
            modifier = Modifier
                .width(IntrinsicSize.Min)
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(
                Icons.Default.KeyboardArrowUp,
                modifier = Modifier.clickable {
                    state.incrementer()
                },
                contentDescription = "number_field_value_incrementer"
            )
            HorizontalDivider()
            Icon(
                Icons.Default.KeyboardArrowDown,
                modifier = Modifier.clickable {
                    state.decrementer()
                },
                contentDescription = "number_field_value_decrementer"
            )
        }
    }
}

@Composable
inline fun <reified T : Number> NumberPickerField(
    modifier: Modifier = Modifier,
    fieldProperties: FieldProperties<T>,
    state: NumberCreationState<T>,
    fieldName: String,
    label: String,
    unit: String? = null,
    noinline trailingIcon: @Composable (() -> Unit)? = null,
    noinline leadingIcon: @Composable (() -> Unit)? = null,
) {
    FieldRegister(
        fieldProperties = fieldProperties.copy(
            valueState = state.numberState,
        ),
        name = fieldName,
    ) {
        TextFormField(
            modifier = modifier
                .widthIn(max = 160.dp)
                .heightIn(max = 120.dp),
            onValueChange = {},
            value = state.numberState.value.toString(),
            textStyle = typography.displaySmall.copy(
                color = colorScheme.onSurface,
            ),
            decorationBox = { innerTextField ->
                Surface(shape = RoundedCornerShape(8.dp)) {
                    Column(
                        modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(2.dp)
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
                                        .weight(1f)
                                        .fillMaxHeight(),
                                    contentAlignment = Alignment.CenterStart
                                ) {
                                    innerTextField()
                                }

                                unit?.let {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxHeight()
                                            .weight(2f),
                                        contentAlignment = Alignment.CenterStart
                                    ) {
                                        Text(unit)
                                    }
                                }
                            }
                            IncrementerAndDecrementerAction(
                                modifier = Modifier.weight(1f),
                                state = state
                            )
                        }
                    }
                }
            },
        )
    }
}

@Composable
inline fun <reified T : Number> NumberPickerField(
    modifier: Modifier = Modifier,
    fieldProperties: FieldProperties<T>,
    initialValue: T,
    fieldName: String,
    label: String,
    adder: T = defaultStep(initialValue),
    decrementer: T = defaultStep(initialValue),
    unit: String? = null,
    noinline trailingIcon: @Composable (() -> Unit)? = null,
    noinline leadingIcon: @Composable (() -> Unit)? = null,
) {
    val state = rememberNumberCreationState(
        initialValue = initialValue,
        adder = adder,
        decrementer = decrementer
    )

    NumberPickerField(
        modifier = modifier,
        fieldProperties = fieldProperties,
        state = state,
        fieldName = fieldName,
        label = label,
        unit = unit,
        trailingIcon = trailingIcon,
        leadingIcon = leadingIcon
    )
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
