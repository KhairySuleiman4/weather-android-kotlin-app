package com.example.weatherapp.model.repos

import com.example.weatherapp.model.pojos.local.weather.WeatherDetails
import com.example.weatherapp.model.repos.location.LocationRepoImp
import com.example.weatherapp.model.repos.settings.SettingsRepoImp
import com.example.weatherapp.model.repos.weather.WeatherRepoImp
import kotlinx.coroutines.flow.Flow

class AppRepoImp(private val settingsRepo: SettingsRepoImp, private val locationRepo: LocationRepoImp, private val weatherRepo: WeatherRepoImp): AppRepo {
    val lat = locationRepo.lat
    val long = locationRepo.long

    companion object{
        @Volatile
        private var instance: AppRepoImp? = null

        fun getInstance(settingsRepo: SettingsRepoImp, locationRepo: LocationRepoImp, weatherRepo: WeatherRepoImp): AppRepoImp {
            if (instance == null) {
                synchronized(this) {
                    if (instance == null) {
                        instance = AppRepoImp(settingsRepo, locationRepo, weatherRepo)
                    }
                }
            }
            return instance!!
        }
    }

    override fun readLanguageChoice() = settingsRepo.readLanguageChoice()

    override fun readTemperatureUnit() = settingsRepo.readTemperatureUnit()

    override fun readLocationChoice() = settingsRepo.readLocationChoice()

    override fun readWindSpeedUnit() = settingsRepo.readWindSpeedUnit()

    override fun readLatLong() = settingsRepo.readLatLong()

    override fun getUserLocation() = locationRepo.getCurrentLocation()

    override fun areLocationPermissionsGranted() = locationRepo.arePermissionsAllowed()

    override suspend fun getWeatherDetails(lat: Double, long: Double): Flow<WeatherDetails> {
        return weatherRepo.getWeatherDetails(lat, long)
    }

    override suspend fun writeLanguageChoice(lang: String) = settingsRepo.writeLanguageChoice(lang)

    override suspend fun writeTemperatureUnit(temp: String) = settingsRepo.writeTemperatureUnit(temp)

    override suspend fun writeLocationChoice(location: String) = settingsRepo.writeLocationChoice(location)

    override suspend fun writeWindSpeedUnit(wind: String) = settingsRepo.writeWindSpeedUnit(wind)

    override suspend fun writeLatLong(lat: Double, long: Double) = settingsRepo.writeLatLong(lat, long)
}