package ru.itis.terraapp.feature.mainscreen.impl.apiImpl

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import ru.itis.terraapp.feature.mainscreen.api.ui.weatherScreen.WeatherScreenApi
import ru.itis.terraapp.feature.mainscreen.impl.ui.weatherScreen.TempDetailsRoute
import ru.itis.terraapp.feature.mainscreen.impl.viewModel.TempDetailsViewModel

class WeatherScreenApiImpl : WeatherScreenApi {
    
    @Composable
    override fun WeatherScreen(city: String) {
        val viewModel: TempDetailsViewModel = hiltViewModel()

        LaunchedEffect(city) {
            if (city.isNotEmpty()) {
                viewModel.getForecast(city)
            }
        }
        
        /*TempDetailsRoute(
            onNavigateToDetails = { },
            onNavigateToAttraction = { },
            viewModel = viewModel
        )*/
    }
}

