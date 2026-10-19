package com.example.nutritiontracker.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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

@Composable
fun <T : Any> ListPagingSection(
    pagingItems: LazyPagingItems<T>,
    key: ((@JvmSuppressWildcards T) -> Any),
    initialDataError: (@Composable () -> Unit)? = null,
    itemContent: @Composable (T) -> Unit,

    ) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(Spacing.m)
    ) {
        items(
            count = pagingItems.itemCount,
            key = pagingItems.itemKey(key),
        ) { index ->
            val item = pagingItems[index]
            if (item != null) {
                itemContent(item)
            }
        }
        when (pagingItems.loadState.append) {
            is LoadState.Loading -> {
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

            else -> {}
        }
    }

    val refreshState = pagingItems.loadState.refresh
    when (refreshState) {
        is LoadState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is LoadState.Error -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                initialDataError?.let {
                    initialDataError()
                } ?: Text("Default Placeholder")
            }

        }

        else -> {
            if (pagingItems.itemCount == 0) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Tidak ada data makanan ditemukan.")
                }
            }
        }
    }
}
