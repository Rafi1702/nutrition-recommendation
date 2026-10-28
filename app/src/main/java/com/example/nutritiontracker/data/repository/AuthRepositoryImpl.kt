package com.example.nutritiontracker.data.repository

import com.example.nutritiontracker.data.local.LocalTokenDataSource
import com.example.nutritiontracker.data.model.auth.SignInRequestDto
import com.example.nutritiontracker.data.model.auth.SignUpRequestDto
import com.example.nutritiontracker.data.remote.AuthService
import com.example.nutritiontracker.data.utils.safeCall
import com.example.nutritiontracker.domain.model.SignInAuth
import com.example.nutritiontracker.domain.model.SignUpAuth
import com.example.nutritiontracker.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authService: AuthService,
    private val localTokenDataSource: LocalTokenDataSource,
) : AuthRepository {
    override suspend fun signIn(authModel: SignInAuth): Result<Unit> {
        val response = safeCall {
            authService.signIn(
                SignInRequestDto(
                    username = authModel.username,
                    password = authModel.password,
                )
            )
        }

        return response.fold(
            onSuccess = { response ->
                localTokenDataSource.setToken(response.data.accessToken)
                localTokenDataSource.setRefreshToken(response.data.refreshToken)
                Result.success(Unit)
            },
            onFailure = { err ->
                Result.failure(err)
            }
        )
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