package com.example.nutritiontracker.data.utils

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.paging.map
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import kotlinx.coroutines.flow.map

sealed interface PageCursor {
    data class Cursor(val value: String?) : PageCursor
    data class Page(val value: Int = 0) : PageCursor
}

class PagingSource<T : Any>(
    private val initialCursor: PageCursor,
    private val maxItems: Int? = null,
    private val networkCallDelay: Long? = null,
    val networkCall: suspend (PageCursor?) -> Result<PaginatedListResponse<T>>,
) : PagingSource<PageCursor, T>() {

    private var loadedCount = 0

    override suspend fun load(params: LoadParams<PageCursor>): LoadResult<PageCursor, T> {
        val currentCursor = params.key ?: initialCursor

        val response = networkCall.invoke(currentCursor)

        networkCallDelay?.let {
            delay(networkCallDelay)
        }

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

                val cursor = nextKey?.let {
                    when (currentCursor) {
                        is PageCursor.Cursor -> PageCursor.Cursor(nextKey)
                        else -> PageCursor.Page(loadedCount)
                    }
                }

                LoadResult.Page(
                    data = res.data.items,
                    prevKey = null,
                    nextKey = cursor,
                )
            },
            onFailure = { error ->
                LoadResult.Error(error)
            }
        )
    }

    override fun getRefreshKey(state: PagingState<PageCursor, T>): PageCursor? {
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
    networkCallDelay: Long? = null,
    pagingConfig: PagingConfig = defaultPageConfig(),
    initialCursor: PageCursor.Page = PageCursor.Page()
): Flow<PagingData<DATA>> {
    return Pager(
        config = pagingConfig,
        pagingSourceFactory = {
            PagingSource(
                initialCursor = initialCursor,
                networkCallDelay = networkCallDelay,
                networkCall = { cursor ->
                    val targetPage = when (cursor) {
                        is PageCursor.Page -> cursor.value
                        else -> initialCursor.value
                    }

                    safeCall {
                        ioCall(targetPage)
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

fun <JSON : Any, DATA : Any> defaultPagerCreation(
    mapToData: (JSON) -> DATA,
    ioCall: suspend (nextCursor: String?) -> Response<PaginatedListResponse<JSON>>,
    pagingConfig: PagingConfig = defaultPageConfig(),
    networkCallDelay: Long? = null,
    initialCursor: PageCursor.Cursor = PageCursor.Cursor(value = null)
): Flow<PagingData<DATA>> {
    return Pager(
        config = pagingConfig,
        pagingSourceFactory = {
            PagingSource(
                initialCursor = initialCursor,
                networkCallDelay = networkCallDelay,
                networkCall = { cursor ->
                    val nextCursor = when (cursor) {
                        is PageCursor.Cursor -> cursor.value
                        else -> null
                    }
                    safeCall {
                        ioCall(nextCursor)
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

