package com.example.weatherapp.model.remote

import com.example.weatherapp.model.pojos.local.weather.WeatherDetails
import com.example.weatherapp.model.pojos.response.forecast.ForecastResponse
import kotlinx.coroutines.flow.Flow

interface RemoteDataSource {
    suspend fun getWeatherDetails(lat: Double, long: Double): Flow<WeatherDetails>

    suspend fun getForecastDetails(): Flow<ForecastResponse>
}