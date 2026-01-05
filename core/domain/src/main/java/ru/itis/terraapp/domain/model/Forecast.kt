package ru.itis.terraapp.domain.model

import androidx.compose.runtime.Immutable

@Immutable
data class Forecast (
    val dt: String,
    val temp: Float,
    val feelsLike: Float,
    val tempMin: Float,
    val tempMax: Float,
    val mainDesc: String,
    val description: String
)