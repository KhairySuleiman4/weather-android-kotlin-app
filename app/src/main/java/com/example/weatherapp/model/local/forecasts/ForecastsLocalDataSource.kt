package com.example.weatherapp.model.local.forecasts

import com.example.weatherapp.model.pojos.local.forecast.WeatherForecast
import kotlinx.coroutines.flow.Flow

interface ForecastsLocalDataSource {
    fun getForecastsForHome(): Flow<List<WeatherForecast>>
}