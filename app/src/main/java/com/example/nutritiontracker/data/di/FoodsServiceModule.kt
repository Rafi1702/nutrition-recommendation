package com.example.nutritiontracker.data.di

import com.example.nutritiontracker.data.remote.FoodRemoteDataSource
import com.example.nutritiontracker.data.remote.UserProfileDataSource
import com.example.nutritiontracker.data.repository.FoodRepositoryImpl
import com.example.nutritiontracker.domain.repository.FoodRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
object FoodsServiceModule {
    @Provides
    fun provideFoodsService(retrofit: Retrofit): FoodRemoteDataSource{
        return retrofit.create(FoodRemoteDataSource::class.java)
    }
}

@Module
@InstallIn(SingletonComponent::class)
object FoodsRepositoryModule{
    @Provides
    fun provideFoodsRepository(foodRemoteDataSource: FoodRemoteDataSource): FoodRepository{
        return FoodRepositoryImpl(foodRemoteDataSource)
    }
}