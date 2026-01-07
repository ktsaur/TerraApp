package ru.itis.terraapp.data.remote.pojo

import com.google.gson.annotations.SerializedName

class CurrentWeatherResponse (
    @SerializedName("main")
    val mainData: MainData? = null,
    @SerializedName("weather")
    val weather: List<WeatherDescription>?= null
)

class MainData(
    @SerializedName("temp")
    val temp: Float?,
    @SerializedName("feels_like")
    val feelsLike: Float?,
    @SerializedName("temp_min")
    val minTemp: Float?,
    @SerializedName("temp_max")
    val maxTemp: Float?,
    @SerializedName("pressure")
    val pressure:Float?
)

class WeatherDescription(
    @SerializedName("description")
    val description: String?
)