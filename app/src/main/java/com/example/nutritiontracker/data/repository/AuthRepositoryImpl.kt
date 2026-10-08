package com.example.nutritiontracker.data.repository

import com.example.nutritiontracker.data.model.SignInRequestDto
import com.example.nutritiontracker.data.model.SignUpRequestDto
import com.example.nutritiontracker.data.remote.AuthService
import com.example.nutritiontracker.domain.model.SignInAuth
import com.example.nutritiontracker.domain.model.SignUpAuth
import com.example.nutritiontracker.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor (private val authService: AuthService) :
    AuthRepository {
    override suspend fun signIn(authModel: SignInAuth) {
        authService.signIn(
            SignInRequestDto(
                username = authModel.username,
                password = authModel.password,
            )
        )
    }

    override suspend fun signUp(authModel: SignUpAuth) {
        authService.signUp(
            SignUpRequestDto(
                username = authModel.username,
                password = authModel.password,
                confirmPassword = authModel.confirmPassword
            )
        )
    }
}