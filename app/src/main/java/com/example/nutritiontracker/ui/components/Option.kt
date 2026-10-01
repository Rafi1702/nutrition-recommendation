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
    placeholder: @Composable ((String) -> Unit)? = null,
) {
    OptionForm(
        fieldProperties = fieldProperties,
        fieldName = fieldName,
        initialValue = initialValue
    ) { state ->
        Column {
            items.forEach { item ->
                Box(modifier = Modifier.clickable { state.value = item }) {
                    placeholder?.invoke(item)
                }
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
                Box(
                    modifier = Modifier
                        .clickable { state.value = item }
                        .then(
                            if (itemWeight > 0f) {
                                Modifier.weight(itemWeight)
                            } else {
                                Modifier
                            }
                        )) {
                    placeholder?.invoke(item, state.value)
                }
            }
        }
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
