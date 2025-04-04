package com.example.weatherapp.model.repos.forecasts

import com.example.weatherapp.model.pojos.local.Notification
import com.example.weatherapp.model.pojos.local.forecast.WeatherForecast
import kotlinx.coroutines.flow.Flow

interface ForecastsRepo {
    suspend fun getForecastDetails(lat: Double, long: Double): Flow<List<WeatherForecast>>
    fun getAllNotifications(): Flow<List<Notification>>
    suspend fun insertForecastsToDatabase(forecasts: List<WeatherForecast>)
    suspend fun insertNotification(notification: Notification)
    fun getForecastsForHome(): Flow<List<WeatherForecast>>
    fun getFavoriteForecasts(cityId: Int): Flow<List<WeatherForecast>>
    suspend fun deleteFavoriteCityForecasts(cityId: Int)
    suspend fun deleteNotification(time: Long)
}