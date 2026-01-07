package ru.itis.terraapp.data.database.model

import kotlinx.serialization.Serializable

@Serializable
data class WeatherDataModel(
    val currentTemp: Float,
    val description: String
)