package com.example.weatherapp.model.local.weather

import com.example.weatherapp.model.local.WeatherDao
import com.example.weatherapp.model.pojos.local.forecast.WeatherForecast
import com.example.weatherapp.model.pojos.local.weather.WeatherDetails

class WeatherLocalDataSourceImp private constructor(private val dao: WeatherDao): WeatherLocalDataSource {
    companion object{
        @Volatile
        private var instance: WeatherLocalDataSourceImp? = null

        fun getInstance(dao: WeatherDao): WeatherLocalDataSourceImp{
            if(instance == null){
                synchronized(this){
                    if(instance == null){
                        instance = WeatherLocalDataSourceImp(dao)
                    }
                }
            }
            return instance!!
        }
    }

    override fun getWeatherDetailsForHome() = dao.getWeatherDetailsForHome()

    override fun getFavoriteWeatherDetails() = dao.getFavoriteWeatherDetails()

    override suspend fun updateHome(weatherDetails: WeatherDetails, forecasts: List<WeatherForecast>) {
        dao.updateHome(weatherDetails, forecasts)
    }

    override suspend fun insertWeatherDetails(weatherDetails: WeatherDetails) {
        dao.insertWeatherDetails(weatherDetails)
    }

    override suspend fun deleteFavoriteCityWeather(cityId: Int) {
        dao.deleteFavoriteCityWeather(cityId)
    }
}