package com.example.nutritiontracker.data.di

import com.example.nutritiontracker.data.remote.ConsumeLogRemoteDataSource
import com.example.nutritiontracker.data.repository.ConsumeLogRepositoryImpl
import com.example.nutritiontracker.domain.repository.ConsumeLogRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ConsumeLogServiceModule {
    @Singleton
    @Provides
    fun provideConsumeLogService(retrofit: Retrofit): ConsumeLogRemoteDataSource {
        return retrofit.create(ConsumeLogRemoteDataSource::class.java)
    }
}


@Module
@InstallIn(SingletonComponent::class)
object ConsumeLogRepositoryModule{
    @Singleton
    @Provides
    fun provideConsumeLogRepository(consumeLogRemoteDataSource: ConsumeLogRemoteDataSource): ConsumeLogRepository {
        return ConsumeLogRepositoryImpl(consumeLogRemoteDataSource)
    }
}