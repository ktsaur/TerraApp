package ru.itis.terraapp.feature.attractions.state

import androidx.compose.runtime.Immutable
import ru.itis.terraapp.domain.model.Attraction
import ru.itis.terraapp.domain.model.Forecast
import ru.itis.terraapp.domain.model.Weather

@Immutable
data class WeatherUIState (
    val city: String = "",
    val weather: Weather = Weather.EMPTY,
    val forecast: List<Forecast>? = emptyList(),
    val attractions: List<Attraction> = emptyList(),
    val error: Throwable? = null,
    val isLoading: Boolean = true
)

sealed interface TempDetailsEffect {
    data object NavigateBack: TempDetailsEffect
    data class NavigateToTempDetails(val city: String) : TempDetailsEffect
    data class NavigateToAttractionDetails(val attractionId: String) : TempDetailsEffect
    data class ShowToast(val message: Int): TempDetailsEffect
}

sealed class TempDetailsEvent {
    data object OnErrorConfirm: TempDetailsEvent()
    data object GetWeatherBtnClicked: TempDetailsEvent()
    data class CityUpdate(val city: String): TempDetailsEvent()
    data class AttractionClicked(val attractionId: String): TempDetailsEvent()
}