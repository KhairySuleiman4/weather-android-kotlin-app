package com.example.weatherapp.model.remote

import com.example.weatherapp.model.pojos.local.forecast.WeatherForecast
import com.example.weatherapp.model.pojos.local.weather.WeatherDetails
import com.example.weatherapp.model.pojos.response.weather.toWeatherDetails
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn


class FakeRemoteDataSourceImp(private val weatherDetails : MutableList<WeatherDetails>? = mutableListOf()): RemoteDataSource{
    override suspend fun getWeatherDetails(lat: Double, long: Double): Flow<WeatherDetails> {
        return flow {
            val result = weatherDetails?.find {
                it.lat == lat && it.lon == long
            }
            if(result != null) emit(result)
            else throw Exception("Weather Details not Found!")
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun getForecastDetails(
        lat: Double,
        long: Double
    ): Flow<List<WeatherForecast>> {
        TODO("Not yet implemented")
    }

}