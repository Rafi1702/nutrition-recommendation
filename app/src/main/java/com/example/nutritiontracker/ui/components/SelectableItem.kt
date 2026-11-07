package com.example.nutritiontracker.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
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
@Composable
fun <T> SelectableItemsWrapper(
    initialValue: List<T> = emptyList(),
    content: @Composable (List<T>) -> Unit
) {
    val selectableState = remember { SelectableState<T>() }
    Column {
        Row(horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Item Selected: ${selectableState.itemCount}")
            Checkbox(checked = selectableState.isSelectableMode, onCheckedChange = {
                selectableState.setSelectableMode()
            })
        }
        content.invoke(selectableState.items)
    }
}

@Composable
fun <T> SelectableItem(
    item: T,
    selected: Boolean = false,
    onClick: (T) -> Unit = {},
    onLongClick: () -> Unit = {},
    itemContent: @Composable () -> Unit,
) {
    Box(
        Modifier
            .combinedClickable(
                interactionSource = null,
                indication = null,
                onClick = {
                    onClick(item)
                })
            .background(colorScheme.primaryContainer)
    ) {
        itemContent()
    }
}

@Composable
private fun ExampleUse() {
    val name = List(10) { index ->
        "item-$index"
    }
    SelectableItemsWrapper(initialValue = name) { data ->

        Column {
            name.forEach { item ->
                SelectableItem(
                    item = item,
                    selected = data.contains(item),
                ){

                }
            }

        }
    }
}