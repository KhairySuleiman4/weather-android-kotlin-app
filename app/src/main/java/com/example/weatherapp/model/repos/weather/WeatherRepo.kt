package com.example.weatherapp.model.repos.weather

import com.example.weatherapp.model.pojos.local.forecast.WeatherForecast
import com.example.weatherapp.model.pojos.local.weather.WeatherDetails
import kotlinx.coroutines.flow.Flow

interface WeatherRepo {
    suspend fun getWeatherDetails(lat: Double, long: Double): Flow<WeatherDetails>
    fun getWeatherDetailsForHome(): Flow<WeatherDetails>
    suspend fun updateHome(weatherDetails: WeatherDetails, forecasts: List<WeatherForecast>)
}