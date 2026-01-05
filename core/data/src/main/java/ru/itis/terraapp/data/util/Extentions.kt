package ru.itis.terraapp.data.util

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import ru.itis.terraapp.data.database.entity.UserEntity
import ru.itis.terraapp.data.database.model.ForecastDataModel
import ru.itis.terraapp.data.database.model.WeatherDataModel
import ru.itis.terraapp.domain.model.Forecast
import ru.itis.terraapp.domain.model.User
import ru.itis.terraapp.domain.model.Weather

inline fun <reified T : ViewModel> Fragment.lazyViewModel(noinline create: (SavedStateHandle) -> T) =
    viewModels<T> {
        AssistedFactory(this, create)
    }

fun User.toEntity() = UserEntity(userId ?: 0, email = email, password = password, username = username)

fun UserEntity.toUser() = User(id, email = email, password = password, username = username)

fun Weather.toData() = WeatherDataModel(
    currentTemp = currentTemp,
    description = weatherDescription
)

fun WeatherDataModel.toDomain() = Weather(
    currentTemp = currentTemp,
    weatherDescription = description
)

fun Forecast.toData() = ForecastDataModel(
    dt = dt,
    temp = temp,
    feelsLike = feelsLike,
    tempMin = tempMin,
    tempMax = tempMax,
    mainDesc = mainDesc,
    description = description
)

fun ForecastDataModel.toDomain() = Forecast(
    dt = dt,
    temp = temp,
    feelsLike = feelsLike,
    tempMin = tempMin,
    tempMax = tempMax,
    mainDesc = mainDesc,
    description = description
)

fun List<ForecastDataModel>.toDomain() = map { it.toDomain() }