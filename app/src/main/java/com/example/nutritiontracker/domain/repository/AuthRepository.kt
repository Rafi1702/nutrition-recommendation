package com.example.nutritiontracker.domain.repository

import com.example.nutritiontracker.domain.model.SignInAuth
import com.example.nutritiontracker.domain.model.SignUpAuth


interface AuthRepository {
    suspend fun signIn(authModel: SignInAuth): Result<Unit>
    suspend fun signUp(authModel: SignUpAuth): Result<Unit>
}