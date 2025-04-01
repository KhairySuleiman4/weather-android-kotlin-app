package com.example.weatherapp.model.local.weather

import com.example.weatherapp.model.pojos.local.forecast.WeatherForecast
import com.example.weatherapp.model.pojos.local.weather.WeatherDetails
import kotlinx.coroutines.flow.Flow

interface WeatherLocalDataSource {
    fun getWeatherDetailsForHome(): Flow<WeatherDetails>
    fun getAllFavoriteWeatherDetails(): Flow<List<WeatherDetails>>
    fun getFavoriteWeatherDetails(cityId: Int): Flow<WeatherDetails>
    suspend fun updateHome(weatherDetails: WeatherDetails, forecasts: List<WeatherForecast>)
    suspend fun insertWeatherDetails(weatherDetails: WeatherDetails)
    suspend fun deleteFavoriteCityWeather(cityId: Int)
}