package ru.itis.terraapp.data.repository

import ru.itis.terraapp.data.remote.OpenWeatherApi
import ru.itis.terraapp.data.remote.pojo.CurrentWeatherResponse
import ru.itis.terraapp.domain.model.Weather
import ru.itis.terraapp.domain.repositories.WeatherRepository
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val openWeatherApi: OpenWeatherApi
) : WeatherRepository {
    
    override suspend fun getWeatherByCityName(city: String): Weather {
        val response = openWeatherApi.getCurrentWeatherByCityName(city)
            ?: throw IllegalStateException("Weather not found for city: $city")
        
        return response.toDomain()
    }
    
    private fun CurrentWeatherResponse.toDomain(): Weather {
        val temp = mainData?.temp ?: 0f
        val description = weather?.firstOrNull()?.description ?: ""
        
        return Weather(
            currentTemp = temp,
            weatherDescription = description
        )
    }
}

