package com.example.weatherapp.model.repos

import com.example.weatherapp.model.pojos.local.forecast.WeatherForecast
import com.example.weatherapp.model.pojos.local.weather.WeatherDetails
import kotlinx.coroutines.flow.Flow

interface AppRepo {
    fun readLanguageChoice(): Flow<String>
    fun readTemperatureUnit(): Flow<String>
    fun readLocationChoice(): Flow<String>
    fun readWindSpeedUnit(): Flow<String>
    fun readLatLong(): Flow<Pair<String, String>>
    fun getUserLocation()
    fun areLocationPermissionsGranted(): Boolean
    fun isInternetAvailable(): Boolean
    fun getWeatherDetailsForHome(): Flow<WeatherDetails>
    fun getForecastsForHome(): Flow<List<WeatherForecast>>
    fun getFavoriteWeatherDetails(): Flow<List<WeatherDetails>>

    suspend fun getWeatherDetails(lat: Double, long: Double): Flow<WeatherDetails>
    suspend fun getForecastDetails(lat: Double, long: Double): Flow<List<WeatherForecast>>
    suspend fun writeLanguageChoice(lang: String)
    suspend fun writeTemperatureUnit(temp: String)
    suspend fun writeLocationChoice(location: String)
    suspend fun writeWindSpeedUnit(wind: String)
    suspend fun writeLatLong(lat: Double, long: Double)
    suspend fun updateHome(weatherDetails: WeatherDetails, forecasts: List<WeatherForecast>)
    suspend fun insertWeatherDetailsToDatabase(weatherDetails: WeatherDetails)
    suspend fun insertForecastsToDatabase(forecasts: List<WeatherForecast>)
    suspend fun deleteFavoriteCityWeather(cityId: Int)
    suspend fun deleteFavoriteCityForecasts(cityId: Int)
}