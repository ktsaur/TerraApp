package ru.itis.terraapp.feature.mainscreen.impl.ui.mainScreen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.itis.terraapp.feature.mainscreen.api.ui.mainScreen.MainScreenApi
import ru.itis.terraapp.feature.mainscreen.api.util.MainScreenNavigation
import ru.itis.terraapp.feature.mainscreen.impl.state.TempDetailsEvent
import ru.itis.terraapp.feature.mainscreen.impl.ui.CurrentTempRoute
import ru.itis.terraapp.feature.mainscreen.impl.ui.TempDetailsRoute
import ru.itis.terraapp.feature.mainscreen.impl.viewModel.TempDetailsViewModel

object MainScreenRoutes {
    const val CITY_INPUT = "city_input"
    const val WEATHER_DETAILS = "weather_details"
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
                val viewModel: TempDetailsViewModel = hiltViewModel()
                val navigation = createNavigation(navController)
                
                // Обновляем состояние города и загружаем данные
                LaunchedEffect(city) {
                    if (city.isNotEmpty()) {
                        viewModel.onEvent(TempDetailsEvent.CityUpdate(city))
                        viewModel.getForecast(city)
                    }
                }
                
                TempDetailsRoute(
                    onNavigateToDetails = navigation::navigateToWeather,
                    viewModel = viewModel
                )
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

