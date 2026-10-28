package com.example.nutritiontracker.data.model.user

import com.google.gson.annotations.SerializedName

data class UserProfileConsumptionResponseDto(
    @SerializedName("id")
    val id: Int,

    @SerializedName("birth")
    val birth: String,

    @SerializedName("gender")
    val gender: String,

    @SerializedName("weight")
    val weight: Int,

    @SerializedName("height")
    val height: Double?,

    @SerializedName("activity")
    val activity: String,

    @SerializedName("account_id")
    val accountId: Int,

    @SerializedName("account")
    val account: AccountResponseDto? = null,

    @SerializedName("consumes")
    val consumes: List<ConsumeResponseDto>? = emptyList()
)

// Placeholder classes for relationships (if needed)
data class AccountResponseDto(
    @SerializedName("id")
    val id: Int
)

data class ConsumeResponseDto(
    @SerializedName("id")
    val id: Int
)