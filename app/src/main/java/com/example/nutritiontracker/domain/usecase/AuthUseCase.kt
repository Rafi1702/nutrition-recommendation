package com.example.nutritiontracker.domain.usecase

import com.example.nutritiontracker.domain.model.SignInAuth
import com.example.nutritiontracker.domain.model.SignUpAuth
import com.example.nutritiontracker.domain.repository.AuthRepository
import javax.inject.Inject


class SignInUseCase @Inject constructor(private val authRepository: AuthRepository) {
    suspend operator fun invoke(authModel: SignInAuth): Result<Unit> {
       return authRepository.signIn(authModel)
    }
}

class SignUpUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(authModel: SignUpAuth) {
        authRepository.signUp(authModel)
    }
}

