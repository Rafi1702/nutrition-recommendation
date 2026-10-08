package com.example.nutritiontracker.data.di

import com.example.nutritiontracker.data.remote.AuthService
import com.example.nutritiontracker.data.repository.AuthRepositoryImpl
import com.example.nutritiontracker.domain.repository.AuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
object AuthServiceModule {
    @Provides
    fun providesAuthService(retrofit: Retrofit): AuthService{
        return retrofit.create(AuthService::class.java)
    }
}

@Module
@InstallIn(SingletonComponent::class)
object AuthRepositoryModule {
    @Provides
    fun providesAuthRepository(authService: AuthService): AuthRepository{
        return AuthRepositoryImpl(authService = authService)
    }
}