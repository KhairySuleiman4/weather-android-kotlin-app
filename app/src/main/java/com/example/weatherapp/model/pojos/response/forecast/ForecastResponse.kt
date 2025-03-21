package com.example.weatherapp.model.pojos.response.forecast

import com.example.weatherapp.model.pojos.response.weather.Clouds
import com.example.weatherapp.model.pojos.response.weather.Coord
import com.example.weatherapp.model.pojos.response.weather.Main
import com.example.weatherapp.model.pojos.response.weather.Weather
import com.example.weatherapp.model.pojos.response.weather.Wind

data class ForecastResponse(
    val list: List<ForecastItem>,
    val city: City
)

