package com.example.weatherapp.model.pojos.response.weather

import com.example.weatherapp.model.pojos.local.weather.WeatherDetails

data class WeatherResponse(
    val coord: Coord,
    val weather: List<Weather>,
    val main: Main,
    val wind: Wind,
    val clouds: Clouds,
    val sys: Sys,
    val id: Int,
    val name: String
)

fun WeatherResponse.toWeatherDetails(): WeatherDetails {
    return WeatherDetails(
        cityId = id,
        lon = coord.lon,
        lat = coord.lat,
        temp = main.temp.toInt(),
        humidity = main.humidity,
        wind = wind.speed,
        pressure = main.pressure,
        clouds = clouds.all,
        cityName = name,
        country = sys.country,
        icon = weather[0].icon,
        description = weather[0].description,
        isFav = true,
    )
}
