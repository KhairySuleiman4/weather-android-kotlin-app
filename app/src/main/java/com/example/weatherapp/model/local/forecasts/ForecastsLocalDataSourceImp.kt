package com.example.weatherapp.model.local.forecasts

import com.example.weatherapp.model.local.WeatherDao
import com.example.weatherapp.model.pojos.local.Notification
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

    override suspend fun insertNotification(notification: Notification) {
        dao.insertNotification(notification)
    }

    override fun getForecastsForHome() = dao.getForecastsForHome()

    override fun getAllNotifications(): Flow<List<Notification>> {
        return dao.getAllNotifications()
    }

    override fun getFavoriteForecasts(cityId: Int) = dao.getFavoriteForecasts(cityId)

    override suspend fun deleteFavoriteCityForecasts(cityId: Int) {
        dao.deleteFavoriteCityForecasts(cityId)
    }

    override suspend fun deleteNotification(time: Long) {
        dao.deleteNotification(time)
    }
}