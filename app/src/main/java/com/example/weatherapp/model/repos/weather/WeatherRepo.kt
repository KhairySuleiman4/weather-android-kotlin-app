package com.example.weatherapp.model.repos.weather

import com.example.weatherapp.model.pojos.local.weather.WeatherDetails
import kotlinx.coroutines.flow.Flow

interface WeatherRepo {
    suspend fun getWeatherDetails(lat: Double, long: Double): Flow<WeatherDetails>
}