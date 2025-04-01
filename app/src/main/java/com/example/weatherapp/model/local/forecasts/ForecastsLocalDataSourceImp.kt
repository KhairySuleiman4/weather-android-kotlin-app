package com.example.weatherapp.model.local.forecasts

import com.example.weatherapp.model.local.WeatherDao
import com.example.weatherapp.model.pojos.local.forecast.WeatherForecast
import kotlinx.coroutines.flow.Flow

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

    override suspend fun insertForecasts(forecasts: List<WeatherForecast>) {
        dao.insertForecasts(forecasts)
    }

    override fun getForecastsForHome() = dao.getForecastsForHome()

    override fun getFavoriteForecasts() = dao.getFavoriteForecasts()

    override suspend fun deleteFavoriteCityForecasts(cityId: Int) {
        dao.deleteFavoriteCityForecasts(cityId)
    }
}