package com.example.nutritiontracker.ui.components.textfield

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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


@Composable
fun IncrementerAndDecrementerAction() {
    Surface(shape = RoundedCornerShape(16.dp), color = colorScheme.onSurface, contentColor = colorScheme.inverseOnSurface) {
        Column(
            modifier = Modifier
                .width(IntrinsicSize.Min)
                .padding(8.dp)
        ) {
            Icon(
                Icons.Default.KeyboardArrowUp,
                contentDescription = "number_field_value_incrementer"
            )
            HorizontalDivider()
            Icon(
                Icons.Default.KeyboardArrowDown,
                contentDescription = "number_field_value_decrementer"
            )
        }
    }
}


@Composable
inline fun <reified T : Number?> NumberPickerField(
    fieldProperties: FieldProperties<T>,
    initialValue: T,
    fieldName: String,
    noinline trailingIcon: @Composable (() -> Unit)? = null,
    noinline leadingIcon: @Composable (() -> Unit)? = null,
    unit: String? = null,
    label: String,
) {

    val state = rememberTextFieldState(initialValue.toString())

    LaunchedEffect(state.text) {
        val text = state.text.toString()

        val rawParsed: Number? = when (T::class) {
            Int::class -> text.toIntOrNull()
            Long::class -> text.toLongOrNull()
            Double::class -> text.toDoubleOrNull()
            Float::class -> text.toFloatOrNull()
            else -> null
        }

        @Suppress("UNCHECKED_CAST")
        val typedValue = rawParsed as? T ?: initialValue

        val transformedValue = fieldProperties.copy(transformer = { value ->
            when (value) {
                is Int -> {
                    if (value < 0) 0 else value
                }

                is Long -> {
                    if (value < 0L) 0L else value
                }

                is Double -> {
                    if (value < 0.0) 0.0 else value
                }

                is Float -> {
                    if (value < 0f) 0f else value
                }

                is Short -> {
                    if (value < 0) 0.toShort() else value
                }

                is Byte -> {
                    if (value < 0) 0.toByte() else value
                }

                else -> value
            } as T

        }).transformer?.invoke(typedValue) ?: typedValue

        val transformedString = transformedValue?.toString() ?: ""
        if (transformedString != text) {
            state.edit {
                replace(0, length, transformedString)
            }
        }
    }

    FieldRegister(
        fieldProperties = fieldProperties.copy(
            valueProvider = {
                val text = state.text.toString()
                Log.d("[NUMBER_FIELD]", "Observed text: $text")
                when (T::class) {
                    Int::class -> text.toIntOrNull() ?: 0
                    Long::class -> text.toLongOrNull() ?: 0L
                    Double::class -> text.toDoubleOrNull() ?: 0.0
                    Float::class -> text.toFloatOrNull() ?: 0f
                    else -> 0
                } as T
            },
        ),
        name = fieldName,
    ) {
        TextFormField(
            state = state,
            modifier = Modifier.widthIn(max = 160.dp).heightIn(max = 120.dp),
            properties = LocalForm.current.getField(fieldName) ?: fieldProperties,
            textStyle =   typography.displaySmall.copy(
                color = colorScheme.onSurface,

            ),
            decorationBox = { innerTextField ->
                Surface(shape = RoundedCornerShape(8.dp)){
                    Column(modifier = Modifier.padding(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)){
                        Text(label)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .weight(1f),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                innerTextField()
                            }

                            unit?.let{
                                Box(modifier = Modifier.fillMaxHeight().weight(2f), contentAlignment = Alignment.BottomStart){
                                    Text(unit)
                                }
                            }
                            IncrementerAndDecrementerAction()
                        }
                    }
                }
            },
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun NumberPickerFieldPreview() {
    NutritionTrackerTheme {
        // NumberPickerField uses FieldRegister which requires LocalForm to be provided
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
