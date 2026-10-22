package com.example.nutritiontracker.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlin.reflect.KProperty1


@Composable
private fun <T : Any> OptionForm(
    fieldName: KProperty1<T, String>,
    content: @Composable ((state: MutableState<String>) -> Unit),
) {
    val formBuilder = currentForm<T>()
    val state = remember { mutableStateOf("") }
    val existingField = formBuilder.getField(fieldName) ?: FieldProperties(validator = listOf())

    LaunchedEffect(Unit) {
        snapshotFlow { state.value }.collect {
            existingField.onValueStateChange(it)
        }
    }

    FieldRegister(
        fieldProperties = existingField,
        name = fieldName
    ) {
        content.invoke(state)
    }
}

@Composable
fun <T : Any> ColumnOptionForm(
    fieldName: KProperty1<T, String>,
    items: List<String> = emptyList(),
    itemWeight: Float = 0f,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    placeholder: @Composable ((String, Boolean) -> Unit)? = null,
) {
    OptionForm(
        fieldName = fieldName,
    ) { state ->
        Column(
            verticalArrangement = verticalArrangement,
            horizontalAlignment = horizontalAlignment
        ) {
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
                ) { placeholder?.invoke(item, state.value == item) }
            }
        }
    }
}

@Composable
fun <T : Any> RowOptionForm(
    fieldName: KProperty1<T, String>,
    items: List<String> = emptyList(),
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    itemWeight: Float = .0f,
    placeholder: @Composable ((String, Boolean) -> Unit)? = null,
) {
    OptionForm(
        fieldName = fieldName,
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
                    placeholder?.invoke(item, state.value == item)
                }
            }
        }
    }
}


@Composable
private fun <T : Any> MultiOptionForm(
    fieldName: KProperty1<T, List<String>>,
    initialValue: List<String> = emptyList(),
    content: @Composable ((state: MutableState<List<String>>) -> Unit),
) {
    val formBuilder = currentForm<T>()
    val state = remember { mutableStateOf(initialValue) }
    val existingField = formBuilder.getField(fieldName) ?: FieldProperties(validator = listOf())

    LaunchedEffect(Unit) {
        snapshotFlow { state.value }.collect {
            existingField.onValueStateChange(it)
        }
    }

    FieldRegister(
        fieldProperties = existingField,
        name = fieldName
    ) {
        content.invoke(state)
    }
}

@Composable
internal fun <T : Any> MultiSelectionOptions(
    fieldName: KProperty1<T, List<String>>,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    initialValue: List<String> = emptyList(),
    itemWeight: Float = .0f,
    items: List<String> = emptyList(),
    placeholder: @Composable ((String, Boolean) -> Unit)? = null,
) {
    MultiOptionForm(
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
                        if(state.value.contains(item)){
                            state.value = buildList {
                                addAll(state.value)
                                remove(item)
                            }
                        }else{
                            state.value = buildList {
                                addAll(state.value)
                                add(item)
                            }
                        }
                    }
                ) {
                    placeholder?.invoke(item, state.value.contains(item))
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
