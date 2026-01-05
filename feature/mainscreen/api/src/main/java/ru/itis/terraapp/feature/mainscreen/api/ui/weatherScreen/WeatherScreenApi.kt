package ru.itis.terraapp.feature.mainscreen.api.ui.weatherScreen

import androidx.compose.runtime.Composable

/**
 * Публичный API для экрана с детальной информацией о погоде
 */
interface WeatherScreenApi {
    @Composable
    fun WeatherScreen(city: String)
}

