package com.example.nutritiontracker.ui.components

import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Checkbox as M3Checkbox

@Composable
fun CheckBoxForm(
    modifier: Modifier = Modifier,
    name: String,
    fieldProperties: FieldProperties<Boolean>,
    removePadding: Boolean = false,
    onCheckedChange: (Boolean) -> Unit = {}
) {

    val isCheckedState = remember { mutableStateOf(false) }

    val boundProperties = remember(fieldProperties) {
        fieldProperties.copy(
            valueState = isCheckedState
        )
    }


    val checkboxContent = @Composable {
        FieldRegister (fieldProperties = boundProperties, name){
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
        return CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides 0.dp) {
            checkboxContent()
        }
    }

    return checkboxContent()
}