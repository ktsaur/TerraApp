package ru.itis.terraapp.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.itis.terraapp.data.database.entity.WeatherApiEntity
import ru.itis.terraapp.data.database.model.ForecastDataModel
import ru.itis.terraapp.data.database.model.WeatherDataModel

@Dao
interface WeatherApiDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeatherApi(weatherApiEntity: WeatherApiEntity)

    @Query("SELECT * FROM weatherApi WHERE city = :city")
    suspend fun getWeatherApi(city: String): WeatherApiEntity?

    @Query("UPDATE weatherApi SET current_temp = :currentTemp, forecast = :forecast WHERE city = :city")
    suspend fun updateWeatherApi(city: String, currentTemp: WeatherDataModel, forecast: List<ForecastDataModel>)

}