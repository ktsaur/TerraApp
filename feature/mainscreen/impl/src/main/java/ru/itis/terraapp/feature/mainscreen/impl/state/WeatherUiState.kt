package ru.itis.terraapp.feature.mainscreen.impl.state

import androidx.compose.runtime.Immutable
import ru.itis.terraapp.domain.model.Forecast
import ru.itis.terraapp.domain.model.Weather

@Immutable
data class WeatherUIState (
    val city: String = "",
    val weather: Weather = Weather.EMPTY,
    val forecast: List<Forecast>? = emptyList(),
    val error: Throwable? = null,
    val isLoading: Boolean = true
)

sealed class TempDetailsEffect {
    data object NavigateBack: TempDetailsEffect()
    data class NavigateToTempDetails(val city: String) : TempDetailsEffect()
    data class ShowToast(val message: Int): TempDetailsEffect()
}

sealed class TempDetailsEvent {
    data object OnErrorConfirm: TempDetailsEvent()
    data object GetWeatherBtnClicked: TempDetailsEvent()
    data class CityUpdate(val city: String): TempDetailsEvent()
}