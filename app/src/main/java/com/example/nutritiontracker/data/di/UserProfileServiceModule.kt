package com.example.nutritiontracker.data.di

import com.example.nutritiontracker.data.remote.UserProfileDataSource
import com.example.nutritiontracker.data.repository.UserProfileRepositoryImpl
import com.example.nutritiontracker.domain.repository.UserProfileRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit


@Module
@InstallIn(SingletonComponent::class)
object UserProfileServiceModule{
    @Provides
    fun provideUserProfileService(retrofit: Retrofit): UserProfileDataSource{
        return retrofit.create(UserProfileDataSource::class.java)
    }
}

@Module
@InstallIn(SingletonComponent::class)
object UserProfileRepositoryModule{
    @Provides
    fun provideUserProfileRepository(userProfileDataSource: UserProfileDataSource): UserProfileRepository{
        return UserProfileRepositoryImpl(userProfileDataSource)
    }
}