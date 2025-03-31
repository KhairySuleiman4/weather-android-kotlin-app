package com.example.weatherapp.model.local.weather

import android.util.Log
import com.example.weatherapp.model.local.WeatherDao
import com.example.weatherapp.model.pojos.local.forecast.WeatherForecast
import com.example.weatherapp.model.pojos.local.weather.WeatherDetails
import kotlinx.coroutines.flow.Flow

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

    override suspend fun updateHome(weatherDetails: WeatherDetails, forecasts: List<WeatherForecast>) {
        dao.updateHome(weatherDetails, forecasts)
    }
}