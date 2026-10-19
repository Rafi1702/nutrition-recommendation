package com.example.nutritiontracker.data.repository

import androidx.paging.Pager
import androidx.paging.PagingData
import androidx.paging.map
import com.example.nutritiontracker.data.model.consumelog.toConsumeLog
import com.example.nutritiontracker.data.remote.ConsumeLogRemoteDataSource
import com.example.nutritiontracker.data.utils.CursorPaginateQuery
import com.example.nutritiontracker.data.utils.ITEMS_LIMIT
import com.example.nutritiontracker.data.utils.PagingSource
import com.example.nutritiontracker.data.utils.defaultPageConfig
import com.example.nutritiontracker.data.utils.safeCall
import com.example.nutritiontracker.domain.model.ConsumeLog
import com.example.nutritiontracker.domain.repository.ConsumeLogRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ConsumeLogRepositoryImpl @Inject constructor(private val consumeLogRemoteDataSource: ConsumeLogRemoteDataSource) : ConsumeLogRepository {
    override fun getConsumeLog(): Flow<PagingData<ConsumeLog>> {
        return  Pager(
            config = defaultPageConfig(),
            pagingSourceFactory = {
                PagingSource(
                    initialCursor = 1,
                    networkCall = { cursor ->
                        safeCall {
                            consumeLogRemoteDataSource.getConsumeLog(
                                CursorPaginateQuery(cursor = cursor, limit = ITEMS_LIMIT).toMap()
                            )
                        }
                    }
                )
            }
        ).flow.map { pagingData ->
            pagingData.map { consumeResponse ->
                consumeResponse.toConsumeLog()
            }
        }
    }
}