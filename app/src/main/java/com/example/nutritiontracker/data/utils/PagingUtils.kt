package com.example.nutritiontracker.data.utils

import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.PagingState

class PagingSource<T : Any>(
    private val initialCursor: Int = 0,
    private val maxItems: Int? = null,
    val networkCall: suspend (Int) -> Result<CursorPaginate<T>>,
) : PagingSource<Int, T>() {

    private var loadedCount = 0

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
        val currentCursor = params.key ?: initialCursor

        val response = networkCall.invoke(currentCursor)

        return response.fold(
            onSuccess = { data ->
                loadedCount += data.items.size

                val isPartialBatch = data.items.size < params.loadSize
                val reachedMaxLimit = maxItems != null && loadedCount >= maxItems

                val nextKey = if (data.hasMore && data.items.isNotEmpty() && !isPartialBatch && !reachedMaxLimit) {
                    data.nextCursor
                } else {
                    null
                }

                LoadResult.Page(
                    data = data.items,
                    prevKey = null, // Cursor-based pagination is forward-only
                    nextKey = nextKey,
                )
            },
            onFailure = { error ->
                LoadResult.Error(error)
            }
        )
    }

    override fun getRefreshKey(state: PagingState<Int, T>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.let { page ->
                page.prevKey ?: page.nextKey
            }
        }
    }
}

fun defaultPageConfig(): PagingConfig {
    return PagingConfig(
        pageSize = ITEMS_LIMIT,
        initialLoadSize = ITEMS_LIMIT,
        prefetchDistance = 3,
        enablePlaceholders = false,
    )
}
