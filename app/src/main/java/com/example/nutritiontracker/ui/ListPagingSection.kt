package com.example.nutritiontracker.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey
import com.example.nutritiontracker.ui.theme.Spacing
import com.example.nutritiontracker.ui.theme.colorScheme
import kotlinx.coroutines.launch

//The first value of header Props is isSticky
typealias HeaderProps = Pair<Boolean, @Composable () -> Unit>

@Composable
fun <T : Any> ListPagingSection(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues = PaddingValues.Zero,
    pagingItems: LazyPagingItems<T>,
    headerProps: HeaderProps? = null,
    state: LazyListState = rememberLazyListState(),
    key: (Int) -> Any,
    initialDataError: (@Composable (Throwable) -> Unit)? = null,
    emptyDataContent: (@Composable () -> Unit)? = null,
    loadingContent: (@Composable () -> Unit)? = null,
    itemContent: @Composable (Int, T) -> Unit,
) {

    var hasAppendedOnce by remember(pagingItems) { mutableStateOf(false) }

    LaunchedEffect(pagingItems.loadState.append) {
        if (pagingItems.loadState.append is LoadState.Loading) {
            hasAppendedOnce = true
        }
    }

    val scrollToTopButtonVisible =
        remember { derivedStateOf { hasAppendedOnce && state.firstVisibleItemIndex > 0 } }

    val scope = rememberCoroutineScope()

    when (val refreshState = pagingItems.loadState.refresh) {
        is LoadState.Loading -> {
            // Show full-screen loader during initial fetch
            Box(modifier = modifier, contentAlignment = Alignment.Center) {
                (loadingContent ?: { CircularProgressIndicator() }).invoke()
            }
        }

        is LoadState.Error -> {
            // Show error state if initial fetch fails
            Box(modifier = modifier, contentAlignment = Alignment.Center) {
                initialDataError?.let {
                    it(refreshState.error)
                } ?: Text("Terjadi kesalahan memuat data.")
            }
        }

        is LoadState.NotLoading -> {
            // Initial load is done; now safe to render LazyColumn and check item count
            Box(modifier = modifier, contentAlignment = Alignment.Center) {
                if (pagingItems.itemCount < 1) {
                    emptyDataContent?.invoke() ?: Text("Tidak ada data ditemukan.")
                } else {
                    LazyColumn(
                        modifier = modifier,
                        state = state,
                        contentPadding = paddingValues,
                        verticalArrangement = Arrangement.spacedBy(Spacing.m)
                    ) {
                        // Render items
                        headerProps?.let { (isSticky, component) ->
                            if (isSticky) stickyHeader { component() } else item { component() }
                        }
                        items(
                            count = pagingItems.itemCount,
                            key = key,
                        ) { index ->
                            val item = pagingItems[index]
                            if (item != null) {
                                itemContent(index, item)
                            }
                        }
                        // Append loading indicator at the bottom when loading more pages
                        if (pagingItems.loadState.append is LoadState.Loading) {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(Spacing.s),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator()
                                }
                            }
                        }
                    }
                    AnimatedVisibility(
                        visible = scrollToTopButtonVisible.value,
                        enter = fadeIn() + scaleIn(),
                        exit = fadeOut() + scaleOut(),
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(Spacing.m)
                    ) {
                        IconButton(
                            modifier = Modifier.background(
                                colorScheme.primaryContainer,
                                CircleShape
                            ),
                            shape = CircleShape,
                            onClick = {
                                scope.launch {
                                    state.animateScrollToItem(index = 0)
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowUpward,
                                tint = colorScheme.onPrimaryContainer,
                                contentDescription = "scroll_to_first_index"
                            )
                        }
                    }
                }
            }
        }
    }
}


@JvmName("ListPagingSectionWithItemKey")
@Composable
fun <T : Any> ListPagingSection(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues = PaddingValues(horizontal = Spacing.m),
    pagingItems: LazyPagingItems<T>,
    state: LazyListState = rememberLazyListState(),
    headerProps: HeaderProps? = null,
    itemKey: ((@JvmSuppressWildcards T) -> Any), // Overload for item-based key selector
    initialDataError: (@Composable (Throwable) -> Unit)? = null,
    emptyDataContent: (@Composable () -> Unit)? = null,
    loadingContent: (@Composable () -> Unit)? = null,
    itemContent: @Composable (Int, T) -> Unit,
) {
    // Delegate to the primary implementation, wrapping the item key with pagingItems.itemKey

    //TODO: Add Side effect when user scrolls later
    ListPagingSection(
        modifier = modifier,
        paddingValues = paddingValues,
        state = state,
        pagingItems = pagingItems,
        headerProps = headerProps,
        key = pagingItems.itemKey(itemKey),
        initialDataError = initialDataError,
        emptyDataContent = emptyDataContent,
        loadingContent = loadingContent,
        itemContent = itemContent
    )
}
