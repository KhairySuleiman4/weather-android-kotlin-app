package com.example.weatherapp.model.remote

import com.example.weatherapp.model.pojos.response.forecast.ForecastResponse
import com.example.weatherapp.model.pojos.response.weather.WeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface APIServices {
    @GET("weather")
    suspend fun getWeatherDetails(
        @Query("lat") lat: Double,
        @Query("lon") long: Double
    ): Response<WeatherResponse>

    @GET("forecast")
    suspend fun getForecastDetails(
        @Query("lat") lat: Double,
        @Query("lon") long: Double
    ): Response<ForecastResponse>
}