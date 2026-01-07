package ru.itis.terraapp.data.remote.pojo

import com.google.gson.annotations.SerializedName

class ForecastWeatherResponse (
    @SerializedName("list")
    val weaterList: List<Weather>? = null
)

class Weather( //для каждого элемента в списке прогноза
    @SerializedName("dt")
    val dt: Long?,
    @SerializedName("main")
    val mainData: MainForecastData? = null,
    @SerializedName("weather")
    val weatherCondition: List<WeatherCondition>? = null
)

class MainForecastData( //температура
    @SerializedName("temp")
    val temp: Float?,
    @SerializedName("feels_like")
    val feelsLike: Float?,
    @SerializedName("temp_min")
    val tempMin: Float?,
    @SerializedName("temp_max")
    val tempMax: Float?,
)

class WeatherCondition ( //описание
    @SerializedName("main")
    val mainDesc: String?,
    @SerializedName("description")
    val description: String?
)