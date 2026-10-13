package com.example.nutritiontracker.ui.components

import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlin.reflect.KProperty1
import androidx.compose.material3.Checkbox as M3Checkbox

@Composable
fun <T : Any> CheckBoxForm(
    modifier: Modifier = Modifier,
    fieldName: KProperty1<T, Boolean>,
    removePadding: Boolean = false,
    onCheckedChange: (Boolean) -> Unit = {}
) {
    val formBuilder = currentForm<T>()

    val isCheckedState = remember { mutableStateOf(false) }

    val existingField = formBuilder.getField(fieldName) ?: FieldProperties(validator = listOf())

    LaunchedEffect(Unit) {
        snapshotFlow { isCheckedState.value }.collect { existingField.onValueStateChange(it) }
    }

    val checkboxContent = @Composable {
        FieldRegister(
            fieldProperties = existingField,
            name = fieldName,
        ) {
            M3Checkbox(
                modifier = modifier,
                checked = isCheckedState.value,
                onCheckedChange = {
                    isCheckedState.value = it
                    onCheckedChange(it)
                }
            )
        }
    }
    if (removePadding) {
        CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides 0.dp) {
            checkboxContent()
        }
    } else {
        checkboxContent()
    }
}
