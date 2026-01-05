package ru.itis.terraapp.feature.mainscreen.api

import androidx.compose.runtime.Composable
import ru.itis.terraapp.feature.mainscreen.api.ui.mainScreen.MainScreenApi
import ru.itis.terraapp.feature.mainscreen.api.ui.weatherScreen.WeatherScreenApi
import ru.itis.terraapp.feature.mainscreen.api.util.MainScreenNavigation

/**
 * Главный публичный API для feature:mainscreen
 * Предоставляет доступ к экранам и навигации
 */
interface MainScreenFeature {
    val mainScreenApi: MainScreenApi
    val weatherScreenApi: WeatherScreenApi
    
    /**
     * Создает функцию навигации для использования внутри feature
     */
    fun createNavigation(navigateToWeather: (String) -> Unit, navigateBack: () -> Unit): MainScreenNavigation
}

