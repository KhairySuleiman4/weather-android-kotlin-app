package com.example.weatherapp.model.local.forecasts

import com.example.weatherapp.model.pojos.local.Notification
import com.example.weatherapp.model.pojos.local.forecast.WeatherForecast
import kotlinx.coroutines.flow.Flow

interface ForecastsLocalDataSource {
    suspend fun insertForecasts(forecasts: List<WeatherForecast>)
    suspend fun insertNotification(notification: Notification)
    fun getForecastsForHome(): Flow<List<WeatherForecast>>
    fun getAllNotifications(): Flow<List<Notification>>
    fun getFavoriteForecasts(cityId: Int): Flow<List<WeatherForecast>>
    suspend fun deleteFavoriteCityForecasts(cityId: Int)
    suspend fun deleteNotification(time: Long)
}