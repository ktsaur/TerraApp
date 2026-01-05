package ru.itis.terraapp.feature.mainscreen.api.util

/**
 * Интерфейс для навигации внутри feature:mainscreen
 */
interface MainScreenNavigation {
    fun navigateToWeather(city: String)
    fun navigateBack()
}

