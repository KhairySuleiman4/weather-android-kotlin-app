package com.example.weatherapp.model.repos.weather

import com.example.weatherapp.model.local.weather.WeatherLocalDataSource
import com.example.weatherapp.model.pojos.local.forecast.WeatherForecast
import com.example.weatherapp.model.pojos.local.weather.WeatherDetails
import com.example.weatherapp.model.remote.RemoteDataSource
import kotlinx.coroutines.flow.Flow

class WeatherRepoImp(private val remote: RemoteDataSource, private val local: WeatherLocalDataSource): WeatherRepo {

    companion object{
        @Volatile
        private var instance: WeatherRepoImp? = null

        fun getInstance(remote: RemoteDataSource, local: WeatherLocalDataSource): WeatherRepoImp {
            if (instance == null) {
                synchronized(this) {
                    if (instance == null) {
                        instance = WeatherRepoImp(remote, local)
                    }
                }
            }
            return instance!!
        }
    }

    override suspend fun getWeatherDetails(lat: Double, long: Double) = remote.getWeatherDetails(lat, long)

    override fun getWeatherDetailsForHome() = local.getWeatherDetailsForHome()

    override fun getFavoriteWeatherDetails() = local.getFavoriteWeatherDetails()

    override suspend fun deleteFavoriteCityWeather(cityId: Int) {
        local.deleteFavoriteCityWeather(cityId)
    }

    override suspend fun updateHome(
        weatherDetails: WeatherDetails,
        forecasts: List<WeatherForecast>
    ) {
        local.updateHome(weatherDetails, forecasts)
    }

    override suspend fun insertWeatherDetailsToDatabase(weatherDetails: WeatherDetails) {
        local.insertWeatherDetails(weatherDetails)
    }
}