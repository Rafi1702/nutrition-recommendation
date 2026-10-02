package com.example.nutritiontracker.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier


@Composable
private fun OptionForm(
    fieldProperties: FieldProperties<String>,
    initialValue: String,
    fieldName: String,
    content: @Composable ((state: MutableState<String>) -> Unit),
) {
    val state = remember { mutableStateOf(initialValue) }

    FieldRegister(
        fieldProperties = fieldProperties.copy(valueProvider = { state.value }),
        name = fieldName
    ) {
        content.invoke(state)
    }
}

@Composable
fun ColumnOptionForm(
    fieldProperties: FieldProperties<String>,
    fieldName: String,
    initialValue: String = "",
    items: List<String> = emptyList(),
    itemWeight: Float = 0f,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    placeholder: @Composable ((String, String) -> Unit)? = null,
) {
    OptionForm(
        fieldProperties = fieldProperties,
        fieldName = fieldName,
        initialValue = initialValue
    ) { state ->
        Column(verticalArrangement = verticalArrangement, horizontalAlignment = horizontalAlignment) {
            items.forEach { item ->
                ClickableBox(
                    modifier = Modifier.then(
                        if (itemWeight > 0f) {
                            Modifier.weight(itemWeight)
                        } else {
                            Modifier
                        }
                    ),
                    onClick = {
                        state.value = item
                    }
                ) { placeholder?.invoke(item, state.value) }
            }
        }
    }
}

@Composable
fun RowOptionForm(
    fieldProperties: FieldProperties<String>,
    fieldName: String,
    items: List<String> = emptyList(),
    initialValue: String = "",
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    itemWeight: Float = .0f,
    placeholder: @Composable ((String, String) -> Unit)? = null,
) {
    OptionForm(
        fieldProperties = fieldProperties,
        fieldName = fieldName,
        initialValue = initialValue
    ) { state ->
        Row(horizontalArrangement = horizontalArrangement) {
            items.forEach { item ->
                ClickableBox(
                    modifier = Modifier.then(
                        if (itemWeight > 0f) {
                            Modifier.weight(itemWeight)
                        } else {
                            Modifier
                        }
                    ),
                    onClick = {
                        state.value = item
                    }
                ) {
                    placeholder?.invoke(item, state.value)
                }
            }
        }
    }
}

@Composable
private fun ClickableBox(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    content: @Composable (() -> Unit)
) {
    Box(
        modifier = modifier
            .clickable(onClick = onClick)
    ) {
        content()
    }
}

class OptionFieldValidator(private val errorMessage: String = "You have to select") :
    FieldValidator<String> {
    override fun validate(value: String): String? {
        val message = when {
            value.isBlank() -> errorMessage
            else -> null
        }

        return message
    }
}
