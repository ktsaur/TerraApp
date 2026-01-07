package ru.itis.terraapp.domain.repositories

import ru.itis.terraapp.domain.model.Weather


interface WeatherRepository {

    suspend fun getWeatherByCityName(city: String): Weather
}