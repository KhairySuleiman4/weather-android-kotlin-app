package com.example.weatherapp.model.repos

import com.example.weatherapp.model.pojos.local.Notification
import com.example.weatherapp.model.pojos.local.forecast.WeatherForecast
import com.example.weatherapp.model.pojos.local.weather.WeatherDetails
import com.example.weatherapp.model.repos.forecasts.ForecastsRepoImp
import com.example.weatherapp.model.repos.location.LocationRepoImp
import com.example.weatherapp.model.repos.settings.SettingsRepoImp
import com.example.weatherapp.model.repos.weather.WeatherRepoImp
import kotlinx.coroutines.flow.Flow

class AppRepoImp(
    private val settingsRepo: SettingsRepoImp,
    private val locationRepo: LocationRepoImp,
    private val weatherRepo: WeatherRepoImp,
    private val forecastRepo: ForecastsRepoImp
): AppRepo {
    val lat = locationRepo.lat
    val long = locationRepo.long

    companion object{
        @Volatile
        private var instance: AppRepoImp? = null

        fun getInstance(
            settingsRepo: SettingsRepoImp,
            locationRepo: LocationRepoImp,
            weatherRepo: WeatherRepoImp,
            forecastRepo: ForecastsRepoImp
        ): AppRepoImp {
            if (instance == null) {
                synchronized(this) {
                    if (instance == null) {
                        instance =
                            AppRepoImp(
                                settingsRepo,
                                locationRepo,
                                weatherRepo,
                                forecastRepo)
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

    override fun isInternetAvailable() = locationRepo.isInternetAvailable()

    override fun getWeatherDetailsForHome() = weatherRepo.getWeatherDetailsForHome()

    override fun getForecastsForHome() = forecastRepo.getForecastsForHome()

    override fun getAllFavoriteWeatherDetails() = weatherRepo.getAllFavoriteWeatherDetails()

    override fun getFavoriteWeatherDetails(cityId: Int) = weatherRepo.getFavoriteWeatherDetails(cityId)

    override fun getFavoriteForecasts(cityId: Int) = forecastRepo.getFavoriteForecasts(cityId)

    override fun getAllNotifications() = forecastRepo.getAllNotifications()

    override suspend fun getWeatherDetails(lat: Double, long: Double) = weatherRepo.getWeatherDetails(lat, long)

    override suspend fun getForecastDetails(lat: Double, long: Double) = forecastRepo.getForecastDetails(lat, long)

    override suspend fun writeLanguageChoice(lang: String) = settingsRepo.writeLanguageChoice(lang)

    override suspend fun writeTemperatureUnit(temp: String) = settingsRepo.writeTemperatureUnit(temp)

    override suspend fun writeLocationChoice(location: String) = settingsRepo.writeLocationChoice(location)

    override suspend fun writeWindSpeedUnit(wind: String) = settingsRepo.writeWindSpeedUnit(wind)

    override suspend fun writeLatLong(lat: Double, long: Double) = settingsRepo.writeLatLong(lat, long)

    override suspend fun updateHome(weatherDetails: WeatherDetails, forecasts: List<WeatherForecast>) {
        weatherRepo.updateHome(weatherDetails, forecasts)
    }

    override suspend fun insertWeatherDetailsToDatabase(weatherDetails: WeatherDetails) {
        weatherRepo.insertWeatherDetailsToDatabase(weatherDetails)
    }

    override suspend fun insertForecastsToDatabase(forecasts: List<WeatherForecast>) {
        forecastRepo.insertForecastsToDatabase(forecasts)
    }

    override suspend fun insertNotification(notification: Notification) {
        forecastRepo.insertNotification(notification)
    }

    override suspend fun deleteFavoriteCityWeather(cityId: Int) {
        weatherRepo.deleteFavoriteCityWeather(cityId)
    }

    override suspend fun deleteFavoriteCityForecasts(cityId: Int) {
        forecastRepo.deleteFavoriteCityForecasts(cityId)
    }

    override suspend fun deleteNotification(time: Long) {
        forecastRepo.deleteNotification(time)
    }
}