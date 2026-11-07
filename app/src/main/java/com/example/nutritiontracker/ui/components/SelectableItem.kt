package com.example.nutritiontracker.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.nutritiontracker.ui.theme.Spacing
import com.example.nutritiontracker.ui.theme.colorScheme
import okhttp3.internal.toImmutableList

class SelectableState<T>(isSelectableMode: Boolean = false, initialValue: List<T> = emptyList()) {
    private val _items = mutableStateOf(initialValue)
    private val isSelectableState = mutableStateOf(isSelectableMode)

    val items = _items.value.toImmutableList()
    val isSelectableMode = isSelectableState.value

    val itemCount = _items.value.size

    fun setSelectableMode() {
        if (isSelectableState.value) {
            _items.value = emptyList()
        }
        isSelectableState.value = !isSelectableState.value
    }

    fun addItems(item: T) {
        _items.value + item
    }
}


/*
*
*   SelectableItemsWrapper{
*
* }
*
*
*
*
*
*
*
*
*
*
* */

private val LocalSelectableState = compositionLocalOf<SelectableState<*>> { error("No SelectableState provideds") }

@Composable
fun <T> SelectableItemsWrapper(
    initialValue: List<T> = emptyList(),
    content: @Composable (List<T>) -> Unit
) {
    val selectableState = remember { SelectableState(initialValue = initialValue) }
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Item Selected: ${selectableState.itemCount}")
            Checkbox(checked = selectableState.isSelectableMode, onCheckedChange = {
                selectableState.setSelectableMode()
            })
        }
        CompositionLocalProvider(LocalSelectableState provides selectableState){
            content.invoke(selectableState.items)
        }
    }
}

@Composable
fun <T> SelectableItem(
    modifier: Modifier = Modifier,
    item: T,
    selected: Boolean = false,
    onClick: (T) -> Unit = {},
    onLongClick: () -> Unit = {},
    itemContent: @Composable () -> Unit,
) {
    Box(
        modifier = modifier
            .combinedClickable(
                interactionSource = null,
                indication = null,
                onLongClick = onLongClick,
                onClick = {
                    onClick(item)
                })
            .background(if (selected) colorScheme.primaryContainer else Color.Unspecified)
    ) {
        itemContent()
    }
}

@Preview(showBackground = true)
@Composable
private fun ExampleUse() {
    val name = List(10) { index ->
        "item-$index"
    }
    SelectableItemsWrapper(initialValue = listOf("item-1")) { data ->
        Column(verticalArrangement = Arrangement.spacedBy(Spacing.m)) {
            name.forEach { item ->
                SelectableItem(
                    modifier = Modifier.fillMaxWidth(),
                    item = item,
                    selected = data.contains(item),
                ) {
                    Text(item)
                }
            }

        }
    }
}