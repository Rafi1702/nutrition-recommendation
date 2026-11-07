package com.example.nutritiontracker.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey
import com.example.nutritiontracker.ui.theme.Spacing

//The first value of header Props is isSticky
typealias HeaderProps = Pair<Boolean, @Composable () -> Unit>

@Composable
fun <T : Any> ListPagingSection(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues = PaddingValues(horizontal = Spacing.m),
    pagingItems: LazyPagingItems<T>,
    headerProps: HeaderProps? = null,
    key: (Int) -> Any,
    initialDataError: (@Composable (Throwable) -> Unit)? = null,
    emptyDataContent: (@Composable () -> Unit)? = null,
    itemContent: @Composable (T) -> Unit,
) {
    when (val refreshState = pagingItems.loadState.refresh) {
        is LoadState.Loading -> {
            // Show full-screen loader during initial fetch
            Box(modifier = modifier, contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
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
            LazyColumn(
                modifier = modifier,
                contentPadding = paddingValues,
                verticalArrangement = Arrangement.spacedBy(Spacing.m)
            ) {
                headerProps?.let { (isSticky, component) ->
                    if (isSticky) stickyHeader { component() } else item { component() }
                }

                if (pagingItems.itemCount < 1) {
                    // List is genuinely empty after loading finished
                    item {
                        emptyDataContent?.invoke() ?: Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(Spacing.l),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Tidak ada data makanan ditemukan.")
                        }
                    }
                } else {
                    // Render items
                    items(
                        count = pagingItems.itemCount,
                        key = key,
                    ) { index ->
                        val item = pagingItems[index]
                        if (item != null) {
                            itemContent(item)
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
    headerProps: HeaderProps? = null,
    itemKey: ((@JvmSuppressWildcards T) -> Any), // Overload for item-based key selector
    initialDataError: (@Composable (Throwable) -> Unit)? = null,
    emptyDataContent: (@Composable () -> Unit)? = null,
    itemContent: @Composable (T) -> Unit,
) {
    // Delegate to the primary implementation, wrapping the item key with pagingItems.itemKey
    ListPagingSection(
        modifier = modifier,
        paddingValues = paddingValues,
        pagingItems = pagingItems,
        headerProps = headerProps,
        key = pagingItems.itemKey(itemKey),
        initialDataError = initialDataError,
        emptyDataContent = emptyDataContent,
        itemContent = itemContent
    )
}
