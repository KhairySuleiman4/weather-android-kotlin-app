package com.example.weatherapp.model.repos.settings

import com.example.weatherapp.model.settingshelper.SettingsHelper

class SettingsRepoImp private constructor(private val helper: SettingsHelper): SettingsRepo {

    companion object{
        @Volatile
        private var instance: SettingsRepoImp? = null

        fun getInstance(helper: SettingsHelper): SettingsRepoImp {
            if (instance == null) {
                synchronized(this) {
                    if (instance == null) {
                        instance = SettingsRepoImp(helper)
                    }
                }
            }
            return instance!!
        }
    }

    override fun readLanguageChoice() = helper.language

    override fun readTemperatureUnit() = helper.temperatureUnit

    override fun readLocationChoice() = helper.location

    override fun readWindSpeedUnit() = helper.windSpeedUnit

    override fun readLatLong() = helper.latLong


    override suspend fun writeLanguageChoice(lang: String) = helper.writeLanguage(lang)

    override suspend fun writeTemperatureUnit(temp: String) = helper.writeTemperatureUnit(temp)

    override suspend fun writeLocationChoice(location: String) = helper.writeLocation(location)

    override suspend fun writeWindSpeedUnit(wind: String) = helper.writeWindSpeedUnit(wind)

    override suspend fun writeLatLong(lat: Double, long: Double) = helper.writeLatLong(lat, long)

}