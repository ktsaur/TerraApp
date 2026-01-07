package ru.itis.terraapp.domain.model

data class User (
    val userId: Int? = null,
    val username: String,
    val email: String,
    val password: String,
)