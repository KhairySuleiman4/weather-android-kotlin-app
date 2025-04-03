package com.example.weatherapp.model.local.weather

import com.example.weatherapp.model.pojos.local.forecast.WeatherForecast
import com.example.weatherapp.model.pojos.local.weather.WeatherDetails
import kotlinx.coroutines.flow.Flow

class FakeWeatherLocalDataSourceImp(private val weatherDetails: MutableList<WeatherDetails>? = mutableListOf()): WeatherLocalDataSource{
    override fun getWeatherDetailsForHome(): Flow<WeatherDetails> {
        TODO("Not yet implemented")
    }

    override fun getAllFavoriteWeatherDetails(): Flow<List<WeatherDetails>> {
        TODO("Not yet implemented")
    }

    override fun getFavoriteWeatherDetails(cityId: Int): Flow<WeatherDetails> {
        TODO("Not yet implemented")
    }

    override suspend fun updateHome(
        weatherDetails: WeatherDetails,
        forecasts: List<WeatherForecast>
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun insertWeatherDetails(weatherDetails: WeatherDetails) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteFavoriteCityWeather(cityId: Int) {
        TODO("Not yet implemented")
    }
}