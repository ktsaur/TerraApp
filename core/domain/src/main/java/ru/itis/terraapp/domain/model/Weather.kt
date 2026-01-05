package ru.itis.terraapp.domain.model

data class Weather(
    val currentTemp: Float,
    val weatherDescription: String
) {
    companion object {
        val EMPTY = Weather(
            currentTemp = 0.0f,
            weatherDescription = "",
        )
    }
}