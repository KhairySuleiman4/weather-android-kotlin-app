package com.example.weatherapp.model.repos.settings

import kotlinx.coroutines.flow.Flow

interface SettingsRepo {
    fun readLanguageChoice(): Flow<String>
    fun readTemperatureUnit(): Flow<String>
    fun readLocationChoice(): Flow<String>
    fun readWindSpeedUnit(): Flow<String>
    fun readLatLong(): Flow<Pair<String, String>>

    suspend fun writeLanguageChoice(lang: String)
    suspend fun writeTemperatureUnit(temp: String)
    suspend fun writeLocationChoice(location: String)
    suspend fun writeWindSpeedUnit(wind: String)
    suspend fun writeLatLong(lat: Double, long: Double)
}