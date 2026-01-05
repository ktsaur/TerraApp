package ru.itis.terraapp.data.database.repository

import ru.itis.terraapp.data.database.dao.WeatherApiDao
import ru.itis.terraapp.data.database.entity.WeatherApiEntity
import ru.itis.terraapp.data.database.model.ForecastDataModel
import ru.itis.terraapp.data.database.model.WeatherDataModel
import javax.inject.Inject

class WeatherApiRepository @Inject constructor(
    private val weatherApiDao: WeatherApiDao
){
    suspend fun getWeatherApi(city: String): WeatherApiEntity {
        return weatherApiDao.getWeatherApi(city = city) ?: throw IllegalStateException("Weather not found")
    }

    suspend fun saveWeatherApi(weatherApiEntity: WeatherApiEntity) {
        weatherApiDao.insertWeatherApi(weatherApiEntity = weatherApiEntity)
    }

    suspend fun updateWeatherApi(city: String, currentTemp: WeatherDataModel, forecast: List<ForecastDataModel>) {
        weatherApiDao.updateWeatherApi(city = city, currentTemp = currentTemp, forecast = forecast)
    }
}