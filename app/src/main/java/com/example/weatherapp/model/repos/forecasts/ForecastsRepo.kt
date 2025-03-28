package com.example.weatherapp.model.repos.forecasts

import com.example.weatherapp.model.pojos.local.forecast.WeatherForecast
import kotlinx.coroutines.flow.Flow

interface ForecastsRepo {
    suspend fun getForecastDetails(lat: Double, long: Double): Flow<List<WeatherForecast>>
}