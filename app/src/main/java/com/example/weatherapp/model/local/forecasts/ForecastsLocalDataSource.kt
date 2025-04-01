package com.example.weatherapp.model.local.forecasts

import com.example.weatherapp.model.pojos.local.forecast.WeatherForecast
import kotlinx.coroutines.flow.Flow

interface ForecastsLocalDataSource {
    suspend fun insertForecasts(forecasts: List<WeatherForecast>)
    fun getForecastsForHome(): Flow<List<WeatherForecast>>
    fun getFavoriteForecasts(): Flow<List<WeatherForecast>>
    suspend fun deleteFavoriteCityForecasts(cityId: Int)
}