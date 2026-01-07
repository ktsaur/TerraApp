package ru.itis.terraapp.domain.repositories

import ru.itis.terraapp.domain.model.Forecast

interface ForecastRepository {

    suspend fun getForecastByCityName(city: String): List<Forecast>
}