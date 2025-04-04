package com.example.weatherapp.model.remote

import com.example.weatherapp.model.pojos.local.forecast.WeatherForecast
import com.example.weatherapp.model.pojos.local.weather.WeatherDetails
import kotlinx.coroutines.flow.Flow

interface RemoteDataSource {
    suspend fun getWeatherDetails(lat: Double, long: Double): Flow<WeatherDetails>

    suspend fun getForecastDetails(lat: Double, long: Double): Flow<List<WeatherForecast>>
}