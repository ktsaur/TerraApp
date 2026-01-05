package ru.itis.terraapp.feature.mainscreen.api.ui.weatherScreen

import androidx.compose.runtime.Composable

interface WeatherScreenApi {
    @Composable
    fun WeatherScreen(city: String)
}

