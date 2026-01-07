package ru.itis.terraapp.domain.usecase.forecast

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.itis.terraapp.domain.model.Forecast
import ru.itis.terraapp.domain.repositories.ForecastRepository
import javax.inject.Inject

class GetForecastByCityNameUseCase @Inject constructor(
    private val forecastRepository: ForecastRepository
) {

    suspend operator fun invoke(city: String): List<Forecast> {
        return withContext(Dispatchers.IO) {
            forecastRepository.getForecastByCityName(city = city)
        }
    }
}