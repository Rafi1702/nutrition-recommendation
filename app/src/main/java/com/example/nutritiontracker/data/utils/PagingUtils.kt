package com.example.nutritiontracker.data.utils

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.paging.map
import com.example.nutritiontracker.data.model.food.toFood
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import kotlinx.coroutines.flow.map

class PagingSource<T : Any>(
    private val initialCursor: Int = 0,
    private val maxItems: Int? = null,
    val networkCall: suspend (Int) -> Result<PaginatedListResponse<T>>,
) : PagingSource<Int, T>() {

    private var loadedCount = 0

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
        val currentCursor = params.key ?: initialCursor

        val response = networkCall.invoke(currentCursor)

        return response.fold(
            onSuccess = { res ->
                loadedCount += res.data.items.size

                val isPartialBatch = res.data.items.size < params.loadSize
                val reachedMaxLimit = maxItems != null && loadedCount >= maxItems

                val nextKey =
                    if (res.data.hasMore && res.data.items.isNotEmpty() && !isPartialBatch && !reachedMaxLimit) {
                        res.data.nextCursor
                    } else {
                        null
                    }

                LoadResult.Page(
                    data = res.data.items,
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

fun <JSON : Any, DATA : Any> defaultPagerCreation(
    mapToData: (JSON) -> DATA,
    ioCall: suspend (cursor: Int) -> Response<PaginatedListResponse<JSON>>,
    pagingConfig: PagingConfig = defaultPageConfig()
): Flow<PagingData<DATA>> {
    return Pager(
        config = pagingConfig,
        pagingSourceFactory = {
            PagingSource(
                initialCursor = 1,
                networkCall = { cursor ->
                    safeCall {
                        ioCall(cursor)
                    }
                }
            )
        }
    ).flow.map { pagingData ->
        pagingData.map { response ->
            mapToData(response)
        }
    }
}
