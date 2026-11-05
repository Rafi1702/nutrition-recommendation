package com.example.nutritiontracker.data.di

import com.example.nutritiontracker.data.remote.RecommendationFoodRemoteDataSource
import com.example.nutritiontracker.data.repository.FoodRecommendationRepositoryImpl
import com.example.nutritiontracker.domain.repository.FoodRecommendationRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
object RecommendedFoodsServiceModule {
    @Provides
    fun provideRecommendedFoodsService(retrofit: Retrofit): RecommendationFoodRemoteDataSource {
        return retrofit.create(RecommendationFoodRemoteDataSource::class.java)
    }
}

@Module
@InstallIn(SingletonComponent::class)
object RecommendedFoodsRepositoryModule {
    @Provides
    fun provideRecommendedFoodsRepository(
        recommendationFoodRemoteDataSource: RecommendationFoodRemoteDataSource
    ): FoodRecommendationRepository {
        return FoodRecommendationRepositoryImpl(recommendationFoodRemoteDataSource)
    }
}
