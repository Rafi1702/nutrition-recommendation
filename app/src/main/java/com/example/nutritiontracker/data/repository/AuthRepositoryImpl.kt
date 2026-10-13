package com.example.nutritiontracker.data.repository

import android.util.Log
import com.example.nutritiontracker.data.model.auth.SignInRequestDto
import com.example.nutritiontracker.data.model.auth.SignUpRequestDto
import com.example.nutritiontracker.data.remote.AuthService
import com.example.nutritiontracker.domain.model.SignInAuth
import com.example.nutritiontracker.domain.model.SignUpAuth
import com.example.nutritiontracker.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(private val authService: AuthService) :
    AuthRepository {
    override suspend fun signIn(authModel: SignInAuth): Result<Unit> {
        return try {
            val response = authService.signIn(
                SignInRequestDto(
                    username = authModel.username,
                    password = authModel.password,
                )
            )
            Log.d("[AuthRepositoryImpl]", "Sign In $response")
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun signUp(authModel: SignUpAuth): Result<Unit> {
        return try {
            authService.signUp(
                SignUpRequestDto(
                    username = authModel.username,
                    password = authModel.password,
                    confirmPassword = authModel.confirmPassword
                )
            )
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }

    }
}