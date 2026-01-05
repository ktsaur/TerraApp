package ru.itis.terraapp.feature.mainscreen.impl.ui.weatherScreen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import ru.itis.terraapp.feature.mainscreen.api.ui.weatherScreen.WeatherScreenApi
import ru.itis.terraapp.feature.mainscreen.impl.ui.TempDetailsRoute
import ru.itis.terraapp.feature.mainscreen.impl.viewModel.TempDetailsViewModel

class WeatherScreenApiImpl : WeatherScreenApi {
    
    @Composable
    override fun WeatherScreen(city: String) {
        val viewModel: TempDetailsViewModel = hiltViewModel()
        
        // Загружаем данные для города
        LaunchedEffect(city) {
            if (city.isNotEmpty()) {
                viewModel.getForecast(city)
            }
        }
        
        TempDetailsRoute(
            onNavigateToDetails = { /* не используется здесь */ },
            viewModel = viewModel
        )
    }
}

