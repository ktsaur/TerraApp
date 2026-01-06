package ru.itis.terraapp.feature.mainscreen.impl.apiImpl

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.itis.terraapp.feature.mainscreen.api.ui.mainScreen.MainScreenApi
import ru.itis.terraapp.feature.mainscreen.api.util.MainScreenNavigation
import ru.itis.terraapp.domain.model.Attraction
import ru.itis.terraapp.feature.mainscreen.impl.state.TempDetailsEvent
import ru.itis.terraapp.feature.mainscreen.impl.ui.attraction.AttractionDetailsScreen
import ru.itis.terraapp.feature.mainscreen.impl.ui.mainScreen.CurrentTempRoute
import ru.itis.terraapp.feature.mainscreen.impl.ui.weatherScreen.TempDetailsRoute
import ru.itis.terraapp.feature.mainscreen.impl.viewModel.TempDetailsViewModel

object MainScreenRoutes {
    const val CITY_INPUT = "city_input"
    const val WEATHER_DETAILS = "weather_details"
    const val ATTRACTION_DETAILS = "attraction_details"
}

class MainScreenApiImpl : MainScreenApi {
    
    @Composable
    override fun MainScreen() {
        val navController = rememberNavController()
        
        NavHost(
            navController = navController,
            startDestination = MainScreenRoutes.CITY_INPUT
        ) {
            composable(route = MainScreenRoutes.CITY_INPUT) {
                val viewModel: TempDetailsViewModel = hiltViewModel()
                val navigation = createNavigation(navController)
                
                CurrentTempRoute(
                    onNavigateToDetails = navigation::navigateToWeather,
                    viewModel = viewModel
                )
            }
            
            composable(route = "${MainScreenRoutes.WEATHER_DETAILS}/{city}") { backStackEntry ->
                val city = backStackEntry.arguments?.getString("city") ?: ""
                // Используем ключ навигации для сохранения ViewModel между экранами
                val viewModel: TempDetailsViewModel = hiltViewModel(key = "weather_$city")
                val navigation = createNavigation(navController)
                
                // Обновляем состояние города и загружаем данные
                LaunchedEffect(city) {
                    if (city.isNotEmpty()) {
                        viewModel.onEvent(TempDetailsEvent.CityUpdate(city))
                        viewModel.getForecast(city)
                    }
                }
                
                val uiState by viewModel.uiState.collectAsState()
                
                /*TempDetailsRoute(
                    onNavigateToDetails = navigation::navigateToWeather,
                    onNavigateToAttraction = { attractionId ->
                        navController.navigate("${MainScreenRoutes.ATTRACTION_DETAILS}/${uiState.city}/$attractionId")
                    },
                    viewModel = viewModel
                )*/
            }
            
            composable(route = "${MainScreenRoutes.ATTRACTION_DETAILS}/{city}/{attractionId}") { backStackEntry ->
                val city = backStackEntry.arguments?.getString("city") ?: ""
                val attractionId = backStackEntry.arguments?.getString("attractionId") ?: ""
                // Используем тот же ключ, что и на экране погоды, чтобы получить тот же ViewModel
                val viewModel: TempDetailsViewModel = hiltViewModel(key = "weather_$city")
                val uiState by viewModel.uiState.collectAsState()
                val attraction = uiState.attractions.find { it.id == attractionId }
                
                // Если достопримечательность не найдена, показываем пустой экран или загрузку
                if (attraction != null) {
                    AttractionDetailsScreen(attraction = attraction)
                } else if (uiState.isLoading) {
                    // Показываем загрузку, если данные еще загружаются
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else {
                    // Если достопримечательность не найдена, возвращаемся назад
                    LaunchedEffect(Unit) {
                        navController.popBackStack()
                    }
                }
            }
        }
    }
    
    private fun createNavigation(navController: NavHostController): MainScreenNavigation {
        return object : MainScreenNavigation {
            override fun navigateToWeather(city: String) {
                navController.navigate("${MainScreenRoutes.WEATHER_DETAILS}/$city")
            }
            
            override fun navigateBack() {
                navController.popBackStack()
            }
        }
    }
}

