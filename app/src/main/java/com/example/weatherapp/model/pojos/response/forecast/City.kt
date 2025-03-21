package com.example.weatherapp.model.pojos.response.forecast

import com.example.weatherapp.model.pojos.response.weather.Coord

data class City(
    val id: Int,
    val name: String,
    val coord: Coord,
    val country: String
)