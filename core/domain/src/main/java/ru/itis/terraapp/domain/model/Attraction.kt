package ru.itis.terraapp.domain.model

import androidx.compose.runtime.Immutable

@Immutable
data class Attraction(
    val id: String,
    val name: String,
    val description: String,
    val rating: Float,
    val address: String,
    val imageUrl: String,
    val category: String,
    val latitude: Double,
    val longitude: Double
)

