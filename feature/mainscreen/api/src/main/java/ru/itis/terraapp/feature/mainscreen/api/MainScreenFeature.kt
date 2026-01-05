package ru.itis.terraapp.feature.mainscreen.api

import ru.itis.terraapp.feature.mainscreen.api.ui.mainScreen.MainScreenApi
import ru.itis.terraapp.feature.mainscreen.api.ui.weatherScreen.WeatherScreenApi
import ru.itis.terraapp.feature.mainscreen.api.util.MainScreenNavigation

interface MainScreenFeature {
    val mainScreenApi: MainScreenApi
    val weatherScreenApi: WeatherScreenApi

    fun createNavigation(navigateToWeather: (String) -> Unit, navigateBack: () -> Unit): MainScreenNavigation
}

