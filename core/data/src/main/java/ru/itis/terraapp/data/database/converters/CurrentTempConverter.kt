package ru.itis.terraapp.data.database.converters

import androidx.room.TypeConverter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.itis.terraapp.data.database.model.WeatherDataModel

class CurrentTempConverter {
    private val json = Json {}

    @TypeConverter
    fun fromCurrentTemp(value: WeatherDataModel): String {
        return json.encodeToString(value)
    }

    @TypeConverter
    fun toCurrentTemp(value: String): WeatherDataModel {
        return json.decodeFromString(value)
    }
}