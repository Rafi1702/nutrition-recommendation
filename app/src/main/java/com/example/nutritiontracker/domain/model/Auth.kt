package com.example.nutritiontracker.domain.model


abstract class Auth(
    val username: String,
    val password: String,
)

class SignInAuth(username: String, password: String): Auth(username, password)

class SignUpAuth(val confirmPassword: String, username: String, password: String) :
    Auth(username, password)