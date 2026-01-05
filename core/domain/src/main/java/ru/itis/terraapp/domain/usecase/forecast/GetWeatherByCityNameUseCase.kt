package ru.itis.terraapp.domain.usecase.forecast

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.itis.terraapp.domain.model.Weather
import ru.itis.terraapp.domain.repositories.WeatherRepository
import javax.inject.Inject

class GetWeatherByCityNameUseCase @Inject constructor (
    private val weatherRepository: WeatherRepository
) {

    suspend operator fun invoke(city: String): Weather {
        return withContext(Dispatchers.IO) {
            weatherRepository.getWeatherByCityName(city = city)
        }
    }
}