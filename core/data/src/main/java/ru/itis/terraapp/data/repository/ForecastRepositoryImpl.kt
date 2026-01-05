package ru.itis.terraapp.data.repository

import ru.itis.terraapp.data.remote.OpenWeatherApi
import ru.itis.terraapp.data.remote.pojo.ForecastWeatherResponse
import ru.itis.terraapp.domain.model.Forecast
import ru.itis.terraapp.domain.repositories.ForecastRepository
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class ForecastRepositoryImpl @Inject constructor(
    private val openWeatherApi: OpenWeatherApi
) : ForecastRepository {
    
    override suspend fun getForecastByCityName(city: String): List<Forecast> {
        val response = openWeatherApi.getForecastWeatherByCityName(city)
            ?: throw IllegalStateException("Forecast not found for city: $city")
        
        return response.toDomain()
    }
    
    private fun ForecastWeatherResponse.toDomain(): List<Forecast> {
        return weaterList?.mapNotNull { weather ->
            val mainData = weather.mainData ?: return@mapNotNull null
            val weatherCondition = weather.weatherCondition?.firstOrNull() ?: return@mapNotNull null
            
            Forecast(
                dt = formatTime(weather.dt),
                temp = mainData.temp ?: 0f,
                feelsLike = mainData.feelsLike ?: 0f,
                tempMin = mainData.tempMin ?: 0f,
                tempMax = mainData.tempMax ?: 0f,
                mainDesc = weatherCondition.mainDesc ?: "",
                description = weatherCondition.description ?: ""
            )
        } ?: emptyList()
    }
    
    private fun formatTime(timestamp: Long?): String {
        if (timestamp == null) return "00:00"
        val date = Date(timestamp * 1000)
        val format = SimpleDateFormat("HH:mm", Locale.getDefault())
        return format.format(date)
    }
}

