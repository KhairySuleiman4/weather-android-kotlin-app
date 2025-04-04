package com.example.weatherapp.model.repos.forecasts

import com.example.weatherapp.model.local.forecasts.ForecastsLocalDataSource
import com.example.weatherapp.model.pojos.local.Notification
import com.example.weatherapp.model.pojos.local.forecast.WeatherForecast
import com.example.weatherapp.model.remote.RemoteDataSource
import kotlinx.coroutines.flow.Flow


class ForecastsRepoImp(private val remote: RemoteDataSource, private val local: ForecastsLocalDataSource): ForecastsRepo {
    companion object{
        @Volatile
        private var instance: ForecastsRepoImp? = null

        fun getInstance(remote: RemoteDataSource, local: ForecastsLocalDataSource): ForecastsRepoImp {
            if (instance == null) {
                synchronized(this) {
                    if (instance == null) {
                        instance = ForecastsRepoImp(remote, local)
                    }
                }
            }
            return instance!!
        }
    }

    override suspend fun getForecastDetails(lat: Double, long: Double) = remote.getForecastDetails(lat, long)

    override fun getAllNotifications(): Flow<List<Notification>> {
        return local.getAllNotifications()
    }

    override suspend fun insertForecastsToDatabase(forecasts: List<WeatherForecast>) {
        local.insertForecasts(forecasts)
    }

    override suspend fun insertNotification(notification: Notification) {
        local.insertNotification(notification)
    }

    override fun getForecastsForHome() = local.getForecastsForHome()

    override fun getFavoriteForecasts(cityId: Int) = local.getFavoriteForecasts(cityId)

    override suspend fun deleteFavoriteCityForecasts(cityId: Int) {
        local.deleteFavoriteCityForecasts(cityId)
    }

    override suspend fun deleteNotification(time: Long) {
        local.deleteNotification(time)
    }
}