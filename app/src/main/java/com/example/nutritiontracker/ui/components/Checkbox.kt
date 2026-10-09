package com.example.nutritiontracker.ui.components

import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlin.reflect.KProperty1
import androidx.compose.material3.Checkbox as M3Checkbox

@Composable
fun <T: Any> CheckBoxForm(
    modifier: Modifier = Modifier,
    fieldName: KProperty1<T, *>,
    fieldProperties: FieldProperties<Boolean>? = null,
    removePadding: Boolean = false,
    onCheckedChange: (Boolean) -> Unit = {}
) {

    val isCheckedState = remember { mutableStateOf(false) }

    val formBuilder = currentForm<T>()

    val boundProps = remember(fieldName) {
        val base = formBuilder.getField(fieldName)
            ?: fieldProperties
            ?: FieldProperties(validator = emptyList())
        base.copy(valueState = derivedStateOf { isCheckedState.value })
    }


    val checkboxContent = @Composable {
        FieldRegister (fieldProperties = boundProps, fieldName,){
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