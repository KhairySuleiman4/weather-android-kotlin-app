package com.example.weatherapp.model.repos.weather

import com.example.weatherapp.model.pojos.local.weather.WeatherDetails
import com.example.weatherapp.model.remote.RemoteDataSource
import kotlinx.coroutines.flow.Flow

class WeatherRepoImp(private val remote: RemoteDataSource): WeatherRepo {

    companion object{
        @Volatile
        private var instance: WeatherRepoImp? = null

        fun getInstance(remote: RemoteDataSource): WeatherRepoImp {
            if (instance == null) {
                synchronized(this) {
                    if (instance == null) {
                        instance = WeatherRepoImp(remote)
                    }
                }
            }
            return instance!!
        }
    }

    override suspend fun getWeatherDetails(lat: Double, long: Double): Flow<WeatherDetails> {
        return remote.getWeatherDetails(lat, long)
    }
}