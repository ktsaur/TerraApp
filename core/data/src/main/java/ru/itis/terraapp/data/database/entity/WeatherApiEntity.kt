package ru.itis.terraapp.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.itis.terraapp.data.database.model.ForecastDataModel
import ru.itis.terraapp.data.database.model.WeatherDataModel

@Entity(tableName = "weatherApi")
data class WeatherApiEntity(
    @PrimaryKey
    @ColumnInfo(name = "city")
    val city: String,
    @ColumnInfo(name = "current_temp")
    val currentTemp: WeatherDataModel,
    @ColumnInfo(name = "forecast")
    val forecast: List<ForecastDataModel>
)