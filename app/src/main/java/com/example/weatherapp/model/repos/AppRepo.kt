package com.example.weatherapp.model.repos

import com.example.weatherapp.model.pojos.local.weather.WeatherDetails
import com.google.android.gms.location.LocationCallback
import kotlinx.coroutines.flow.Flow

interface AppRepo {
    fun readLanguageChoice(): Flow<String>
    fun readTemperatureUnit(): Flow<String>
    fun readLocationChoice(): Flow<String>
    fun readWindSpeedUnit(): Flow<String>
    fun readLatLong(): Flow<Pair<String, String>>
    fun getUserLocation()
    fun areLocationPermissionsGranted(): Boolean

    suspend fun getWeatherDetails(lat: Double, long: Double): Flow<WeatherDetails>
    suspend fun writeLanguageChoice(lang: String)
    suspend fun writeTemperatureUnit(temp: String)
    suspend fun writeLocationChoice(location: String)
    suspend fun writeWindSpeedUnit(wind: String)
    suspend fun writeLatLong(lat: Double, long: Double)
}