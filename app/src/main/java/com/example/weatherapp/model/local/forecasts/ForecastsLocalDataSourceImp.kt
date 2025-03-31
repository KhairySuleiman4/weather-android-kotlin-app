package com.example.weatherapp.model.local.forecasts

import com.example.weatherapp.model.local.WeatherDao
import com.example.weatherapp.model.pojos.local.forecast.WeatherForecast

class ForecastsLocalDataSourceImp private constructor(private val dao: WeatherDao): ForecastsLocalDataSource {
    companion object{
        @Volatile
        private var instance: ForecastsLocalDataSourceImp? = null

        fun getInstance(dao: WeatherDao): ForecastsLocalDataSourceImp{
            if(instance == null){
                synchronized(this){
                    if(instance == null){
                        instance = ForecastsLocalDataSourceImp(dao)
                    }
                }
            }
            return instance!!
        }
    }

    override fun getForecastsForHome() = dao.getForecastsForHome()
}