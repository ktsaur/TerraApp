package ru.itis.terraapp.data.database.model

import kotlinx.serialization.Serializable

@Serializable
data class ForecastDataModel(
    val dt: String,
    val temp: Float,
    val feelsLike: Float,
    val tempMin: Float,
    val tempMax: Float,
    val mainDesc: String,
    val description: String
)