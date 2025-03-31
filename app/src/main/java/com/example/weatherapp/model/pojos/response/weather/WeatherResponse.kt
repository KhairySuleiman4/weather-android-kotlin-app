package com.example.weatherapp.model.pojos.response.weather

import com.example.weatherapp.model.pojos.local.weather.WeatherDetails
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class WeatherResponse(
    val coord: Coord,
    val weather: List<Weather>,
    val main: Main,
    val wind: Wind,
    val clouds: Clouds,
    val id: Int,
    val name: String
)

fun WeatherResponse.toWeatherDetails(): WeatherDetails {
    return WeatherDetails(
        cityId = id,
        lon = coord.lon,
        lat = coord.lat,
        temp = main.temp,
        humidity = main.humidity,
        wind = wind.speed,
        pressure = main.pressure,
        clouds = clouds.all,
        cityName = name,
        icon = weather[0].icon,
        description = weather[0].description,
        isFav = false,
        lastUpdate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).format(Date())
    )
}

/*
val cityId: Int,
val lon: Double,
val lat: Double,
val temp: Double,
val humidity: Int,
val wind: Double,
val pressure: Int,
val clouds: Int,
val cityName: String,
val icon: String,
val description: String
val isFav: Boolean,
val lastUpdate: String,
*/



