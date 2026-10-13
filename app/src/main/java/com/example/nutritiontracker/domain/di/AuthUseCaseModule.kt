package com.example.nutritiontracker.domain.di

import com.example.nutritiontracker.domain.repository.AuthRepository
import com.example.nutritiontracker.domain.usecase.SignInUseCase
import com.example.nutritiontracker.domain.usecase.SignUpUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AuthUseCaseModule {
    @Provides
    fun provideSignInUseCase(authRepository: AuthRepository): SignInUseCase{
        return SignInUseCase(authRepository = authRepository)
    }
    @Provides
    fun provideSignUpUseCase(authRepository: AuthRepository): SignUpUseCase {
        return SignUpUseCase(authRepository = authRepository)
    }
}