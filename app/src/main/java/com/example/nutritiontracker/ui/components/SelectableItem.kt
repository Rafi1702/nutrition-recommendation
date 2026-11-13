package com.example.nutritiontracker.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.nutritiontracker.R
import com.example.nutritiontracker.ui.theme.Spacing
import com.example.nutritiontracker.ui.theme.colorScheme

enum class SelectionMode(val index: Int) {
    SINGLE(0),
    BULK(1)
}

val SelectionMode.label: String
    @Composable get() {
        val labels = stringArrayResource(R.array.selection_mode_array)
        return labels.getOrElse(this.index) { this.name }
    }

class SelectableState(
    isSelectableMode: Boolean = false,
    initialValue: Set<String> = emptySet(),
) {
    private val _items = mutableStateOf(initialValue)
    private val isSelectableState = mutableStateOf(isSelectableMode)
    val items get() = _items.value
    val isSelectableMode get() = isSelectableState.value && _items.value.isNotEmpty()
    val itemCount get() = _items.value.size

    fun toggleSelectableMode(item: String? = null) {
        if (isSelectableState.value) {
            _items.value = emptySet()
            isSelectableState.value = false
        } else {
            isSelectableState.value = true
            item?.let {
                _items.value += item
            }
        }
    }

    fun addItems(item: String) {
        if (!isSelectableState.value) return

        if (_items.value.contains(item)) {
            _items.value -= item
        } else {
            _items.value += item
        }
    }
}

val LocalSelectableState =
    compositionLocalOf<SelectableState> { error("No selectable state provided") }

@Composable
fun rememberSelectableState( initialValue: Set<String>): SelectableState {
    return remember { SelectableState(initialValue = initialValue) }
}

@Composable
fun rememberSelectableState(
    key: SelectionMode,
    initialValue: Set<String>,
): SelectableState {
    return remember(key) {
        SelectableState(
            initialValue = initialValue,
            isSelectableMode = key == SelectionMode.BULK
        )
    }
}


@Composable
fun SelectableItemsWrapper(
    initialValue: Set<String> = emptySet(),
    onFloatingButtonClicked: (ids: Set<String>) -> Unit = {},
    isLoading: Boolean? = null,
    content: @Composable (ids: Set<String>) -> Unit
) {
    val selectableState = rememberSelectableState(initialValue = initialValue)

    Box(modifier = Modifier.fillMaxSize()) {
        Column {
            if (selectableState.isSelectableMode) Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Spacing.s),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(stringResource(R.string.item_selected_format, selectableState.itemCount))
                Checkbox(checked = selectableState.isSelectableMode, onCheckedChange = {
                    selectableState.toggleSelectableMode()
                })
            }

            CompositionLocalProvider(LocalSelectableState provides selectableState) {
                content.invoke(selectableState.items)
            }
        }

        if (selectableState.isSelectableMode) Button(
            modifier = Modifier.align(Alignment.BottomCenter),
            onClick = { onFloatingButtonClicked(selectableState.items) }) {
            if (isLoading == true) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp))
            } else {
                Text(stringResource(R.string.add_all))
            }
        }
    }
}

@Composable
fun SelectableItemsWrapper(
    initialValue: Set<String> = emptySet(),
    selectionMode: SelectionMode = SelectionMode.SINGLE,
    header: @Composable ((ids: Set<String>) -> Unit)? = null,
    floatingButton: @Composable ((ids: Set<String>) -> Unit)? = null,
    content: @Composable (ids: Set<String>) -> Unit
) {
    val selectableState = rememberSelectableState(key = selectionMode, initialValue = initialValue)

    Box(modifier = Modifier.fillMaxSize()) {
        Column {
            header?.invoke(selectableState.items)
            CompositionLocalProvider(LocalSelectableState provides selectableState) {
                content.invoke(selectableState.items)
            }
        }

        if (selectionMode == SelectionMode.BULK) Box(modifier = Modifier.align(Alignment.BottomCenter)) {
            floatingButton?.invoke(
                selectableState.items
            )
        }
    }
}

@Composable
fun SelectableItem(
    modifier: Modifier = Modifier,
    itemId: String,
    enableLongClick: Boolean = true,
    onItemClick: (String) -> Unit = {},
    itemContent: @Composable (isSelected: Boolean) -> Unit,
) {
    val stateContainer = LocalSelectableState.current
    Box(
        modifier = modifier
            .fillMaxWidth()
            .combinedClickable(
                interactionSource = null,
                indication = null,
                onLongClick = {
                    if (enableLongClick) {
                        stateContainer.toggleSelectableMode(itemId)
                    }
                },
                onClick = {
                    stateContainer.addItems(itemId)
                    onItemClick(itemId)
                }
            )
            .background(if (stateContainer.items.contains(itemId)) colorScheme.primaryContainer else Color.Unspecified)
    ) {
        itemContent(stateContainer.items.contains(itemId))
    }
}

@Preview(showBackground = true)
@Composable
private fun ExampleUse() {
    val name = List(10) { index ->
        "item-$index"
    }
    SelectableItemsWrapper(initialValue = setOf("item-1")) {
        Column(verticalArrangement = Arrangement.spacedBy(Spacing.m)) {
            name.forEach { item ->
                SelectableItem(
                    modifier = Modifier.fillMaxWidth(),
                    itemId = item
                ) {
                    Text(item)
                }
            }
        }
    }
}

@Composable
private fun BarItem(selected: Boolean = false) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .background(
                    color = if (selected) colorScheme.tertiaryContainer else Color.Unspecified,
                    shape = (RoundedCornerShape(Spacing.l))
                )
                .padding(horizontal = Spacing.m, vertical = Spacing.xs)
        ) {
            Icon(
                Icons.Default.Home,
                contentDescription = null
            )
        }
        Text("Label")
    }
}

@Preview(showBackground = true)
@Composable
private fun ExampleContainer() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .background(colorScheme.primaryContainer),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        List(3) { index -> BarItem(selected = index == 1) }
    }
}
