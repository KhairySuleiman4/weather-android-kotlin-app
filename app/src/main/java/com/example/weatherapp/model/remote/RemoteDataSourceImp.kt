package com.example.weatherapp.model.remote

import com.example.weatherapp.model.pojos.local.weather.WeatherDetails
import com.example.weatherapp.model.pojos.response.forecast.ForecastResponse
import com.example.weatherapp.model.pojos.response.weather.toWeatherDetails
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class RemoteDataSourceImp(private val service: APIServices): RemoteDataSource {

    override suspend fun getWeatherDetails(lat: Double, long: Double): Flow<WeatherDetails> {
        return flow {
            if(service.getWeatherDetails(lat, long).isSuccessful)
                service.getWeatherDetails(lat, long).body()?.let{
                    emit(it.toWeatherDetails())
                }
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun getForecastDetails(): Flow<ForecastResponse> {
        return flow {
            if(service.getForecastDetails().isSuccessful)
                service.getForecastDetails().body()?.let{
                    emit(it)
                }
        }.flowOn(Dispatchers.IO)
    }
}