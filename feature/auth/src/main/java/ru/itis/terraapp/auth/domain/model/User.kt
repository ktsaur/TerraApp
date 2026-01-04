package ru.itis.terraapp.auth.domain.model

data class User (
    val userId: Int? = null,
    val username: String,
    val email: String,
    val password: String,
)