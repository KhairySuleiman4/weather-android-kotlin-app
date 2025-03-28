package com.example.weatherapp.model.repos.forecasts

import com.example.weatherapp.model.pojos.local.forecast.WeatherForecast
import com.example.weatherapp.model.remote.RemoteDataSource
import kotlinx.coroutines.flow.Flow


class ForecastsRepoImp(private val remote: RemoteDataSource): ForecastsRepo {
    companion object{
        @Volatile
        private var instance: ForecastsRepoImp? = null

        fun getInstance(remote: RemoteDataSource): ForecastsRepoImp {
            if (instance == null) {
                synchronized(this) {
                    if (instance == null) {
                        instance = ForecastsRepoImp(remote)
                    }
                }
            }
            return instance!!
        }
    }

    override suspend fun getForecastDetails(
        lat: Double,
        long: Double
    ): Flow<List<WeatherForecast>> {
        return remote.getForecastDetails(lat, long)
    }
}