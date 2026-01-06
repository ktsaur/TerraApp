package ru.itis.terraapp.feature.attractions.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.itis.terraapp.data.database.InceptionDatabase
import ru.itis.terraapp.data.database.entity.QueryHistoryEntity
import ru.itis.terraapp.data.database.entity.WeatherApiEntity
import ru.itis.terraapp.base.AuthManager.AuthManager
import ru.itis.terraapp.data.util.toData
import ru.itis.terraapp.data.util.toDomain
import ru.itis.terraapp.domain.usecase.attractions.GetAttractionsByCityNameUseCase
import ru.itis.terraapp.domain.usecase.favourites.ToggleFavouriteAttractionUseCase
import ru.itis.terraapp.domain.usecase.forecast.GetForecastByCityNameUseCase
import ru.itis.terraapp.domain.usecase.forecast.GetWeatherByCityNameUseCase
import ru.itis.terraapp.feature.attractions.state.TempDetailsEffect
import ru.itis.terraapp.feature.attractions.state.TempDetailsEvent
import ru.itis.terraapp.feature.attractions.state.WeatherUIState
import ru.itis.terraapp.feature.attractions.util.CityValidationException
import javax.inject.Inject
import kotlin.math.abs

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val getForecastByCityNameUseCase: GetForecastByCityNameUseCase,
    private val getWeatherByCityNameUseCase: GetWeatherByCityNameUseCase,
    private val getAttractionsByCityNameUseCase: GetAttractionsByCityNameUseCase,
    private val database: InceptionDatabase,
    private val toggleFavouriteAttractionUseCase: ToggleFavouriteAttractionUseCase,
    private val authManager: AuthManager
) : ViewModel() {
    private val _uiState = MutableStateFlow(WeatherUIState())
    val uiState = _uiState.asStateFlow()

    private val _effectFlow = MutableSharedFlow<TempDetailsEffect>()
    val effectFlow = _effectFlow.asSharedFlow()

    fun onEvent(event: TempDetailsEvent) {
        when(event) {
            is TempDetailsEvent.OnErrorConfirm -> {
                viewModelScope.launch {
                    _effectFlow.emit(TempDetailsEffect.NavigateBack)
                }
            }
            is TempDetailsEvent.CityUpdate -> { _uiState.update { it.copy(city = event.city, error = null) } }
            is TempDetailsEvent.GetWeatherBtnClicked -> {
                getForecast(city = _uiState.value.city)
                viewModelScope.launch {
                    _effectFlow.emit(TempDetailsEffect.NavigateToTempDetails(city = _uiState.value.city))
                }
            }
            is TempDetailsEvent.AttractionClicked -> {
                viewModelScope.launch {
                    _effectFlow.emit(TempDetailsEffect.NavigateToAttractionDetails(event.attractionId))
                }
            }
            is TempDetailsEvent.FavouriteToggle -> {
                toggleFavourite(event.attractionId)
            }
        }
    }

    private fun toggleFavourite(attractionId: String) {
        val attraction = _uiState.value.attractions.find { it.id == attractionId } ?: return
        val userId = authManager.getUserId() ?: return
        viewModelScope.launch {
            runCatching {
                val nowFavourite = toggleFavouriteAttractionUseCase(attraction, userId)
                _uiState.update { state ->
                    val current = state.favouriteAttractionIds.toMutableSet()
                    if (nowFavourite) {
                        current.add(attractionId)
                    } else {
                        current.remove(attractionId)
                    }
                    state.copy(favouriteAttractionIds = current)
                }
            }.onFailure { ex ->
                _uiState.update { it.copy(error = ex) }
            }
        }
    }

    fun getForecast(city: String) {
        if (city.firstOrNull()?.isLowerCase() == true) {
            _uiState.update { it.copy(error = CityValidationException(),  city = city, isLoading = false) }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(error = null,  city = city, isLoading = true) }

            val lastTimestamp = withContext(Dispatchers.IO) {
                database.queryHistoryDao.getLastQueryForCity(city = city)
            }

            val shouldFetchFromApi = withContext(Dispatchers.IO) {
                if (lastTimestamp == null) {
                    return@withContext true
                }
                val countBetween =
                    database.queryHistoryDao.countQueryBetween(
                        start = lastTimestamp,
                        end = System.currentTimeMillis()
                    )
                return@withContext (abs(lastTimestamp - System.currentTimeMillis()) >= 5 * 60 * 1000 || countBetween >= 3)
            }

//            if (shouldFetchFromApi) {
                fetchFromApi(city = city)
            /*} else {
                fetchFromDb(city = city)
            }*/
        }
    }

    private suspend fun insertQueryHistory(query: QueryHistoryEntity) {
        withContext(Dispatchers.IO) { database.queryHistoryDao.insertQuery(queryHistoryEntity = query) }
    }

    private suspend fun fetchFromApi(city: String) {
        runCatching {
            val forecast = getForecastByCityNameUseCase.invoke(city = city)
            val weather = getWeatherByCityNameUseCase.invoke(city = city)
            val attractions = getAttractionsByCityNameUseCase.invoke(city = city)
            Triple(forecast, weather, attractions)
        }.onSuccess { (forecast, weather, attractions) ->
            _uiState.update {
                it.copy(
                    forecast = forecast,
                    weather = weather,
                    attractions = attractions,
                    error = null,
                    isLoading = false
                )
            }
            withContext(Dispatchers.IO) {
                val weatherApiEntity =
                    WeatherApiEntity(
                        city = city,
                        currentTemp = weather.toData(),
                        forecast = forecast.map { it.toData() },
                    )
                database.weatherApiDao.insertWeatherApi(weatherApiEntity)
            }
            insertQueryHistory(
                QueryHistoryEntity(
                    city = city,
                    timestamp = System.currentTimeMillis()
                )
            )
        }.onFailure { ex ->
            _uiState.update { it.copy(error = ex, isLoading = false) }
        }
    }

    private suspend fun fetchFromDb(city: String) {
        withContext(Dispatchers.IO) {
            database.weatherApiDao.getWeatherApi(city)
        }?.let { weatherApiEntity ->
            _uiState.update {
                it.copy(
                    forecast = weatherApiEntity.forecast.toDomain(),
                    weather = weatherApiEntity.currentTemp.toDomain(),
                    error = null,
                    isLoading = false
                )
            }
            insertQueryHistory(
                QueryHistoryEntity(
                    city = city,
                    timestamp = System.currentTimeMillis()
                )
            )
        }
    }
}
