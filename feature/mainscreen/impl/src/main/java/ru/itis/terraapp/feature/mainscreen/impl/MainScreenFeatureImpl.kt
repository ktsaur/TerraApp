package ru.itis.terraapp.feature.mainscreen.impl

import ru.itis.terraapp.feature.mainscreen.api.MainScreenFeature
import ru.itis.terraapp.feature.mainscreen.api.ui.mainScreen.MainScreenApi
import ru.itis.terraapp.feature.mainscreen.api.ui.weatherScreen.WeatherScreenApi
import ru.itis.terraapp.feature.mainscreen.api.util.MainScreenNavigation
import ru.itis.terraapp.feature.mainscreen.impl.apiImpl.MainScreenApiImpl
import ru.itis.terraapp.feature.mainscreen.impl.apiImpl.WeatherScreenApiImpl
import javax.inject.Inject

class MainScreenFeatureImpl @Inject constructor() : MainScreenFeature {
    override val mainScreenApi: MainScreenApi = MainScreenApiImpl()
    override val weatherScreenApi: WeatherScreenApi = WeatherScreenApiImpl()
    
    override fun createNavigation(
        navigateToWeather: (String) -> Unit,
        navigateBack: () -> Unit
    ): MainScreenNavigation {
        return MainScreenNavigationImpl(navigateToWeather, navigateBack)
    }
}

private class MainScreenNavigationImpl(
    private val navigateToWeather: (String) -> Unit,
    private val navigateBack: () -> Unit
) : MainScreenNavigation {
    override fun navigateToWeather(city: String) {
        navigateToWeather(city)
    }
    
    override fun navigateBack() {
        navigateBack()
    }
}

