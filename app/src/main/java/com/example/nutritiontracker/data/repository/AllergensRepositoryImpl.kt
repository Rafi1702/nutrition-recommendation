package com.example.nutritiontracker.data.repository

import com.example.nutritiontracker.data.model.allergens.toAllergens
import com.example.nutritiontracker.data.remote.AllergensRemoteDataSource
import com.example.nutritiontracker.data.utils.safeCall
import com.example.nutritiontracker.domain.model.Allergens
import com.example.nutritiontracker.domain.repository.AllergensRepository
import javax.inject.Inject

class AllergensRepositoryImpl @Inject constructor(private val allergensRemoteDataSource: AllergensRemoteDataSource) :
    AllergensRepository {
    override suspend fun getAllergens(): Result<List<Allergens>> {
        val allergensResponse = safeCall {
            allergensRemoteDataSource.getAllergens()
        }

        return allergensResponse.fold(
            onSuccess = { allergens ->
                Result.success(allergens.data.items.map { allergen -> allergen.toAllergens() })
            },
            onFailure = { err ->
                Result.failure(err)
            }
        )
    }
}