package com.example.weatherapp.model.pojos.response.forecast

import com.example.weatherapp.model.pojos.local.forecast.WeatherForecast
import com.example.weatherapp.model.pojos.response.weather.Clouds
import com.example.weatherapp.model.pojos.response.weather.Coord
import com.example.weatherapp.model.pojos.response.weather.Main
import com.example.weatherapp.model.pojos.response.weather.Weather
import com.example.weatherapp.model.pojos.response.weather.Wind

data class ForecastResponse(
    val list: List<ForecastItem>,
    val city: City
)

fun ForecastResponse.toForecastDetails(): List<WeatherForecast>{
    val listOfForecastItems = this.list
    val listOfWeatherForecasts = mutableListOf<WeatherForecast>()
    for(item in listOfForecastItems){
        listOfWeatherForecasts.add(item.toWeatherForecast())
    }
    for(forecast in listOfWeatherForecasts){
        forecast.cityId = this.city.id
    }
    return listOfWeatherForecasts
}