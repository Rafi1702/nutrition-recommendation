package com.example.nutritiontracker.data.di

import com.example.nutritiontracker.data.remote.AllergensRemoteDataSource
import com.example.nutritiontracker.data.repository.AllergensRepositoryImpl
import com.example.nutritiontracker.domain.repository.AllergensRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
object AllergensServiceModule {

    @Provides
    fun providesAllergensService(retrofit: Retrofit): AllergensRemoteDataSource {
        return retrofit.create(AllergensRemoteDataSource::class.java)
    }

}

@Module
@InstallIn(SingletonComponent::class)
object AllergensRepositoryModule {
    @Provides
    fun provideAllergensRepository(allergensRemoteDataSource: AllergensRemoteDataSource): AllergensRepository {
        return AllergensRepositoryImpl(allergensRemoteDataSource)
    }
}

