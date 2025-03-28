package com.example.weatherapp.model.remote

import com.example.weatherapp.BuildConfig
import com.example.weatherapp.model.pojos.response.forecast.ForecastResponse
import com.example.weatherapp.model.pojos.response.weather.WeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface APIServices {
    @GET("weather")
    suspend fun getWeatherDetails(
        @Query("lat") lat: Double,
        @Query("lon") long: Double,
        @Query("appid") apiKey: String = BuildConfig.WEATHER_API_KEY
    ): Response<WeatherResponse>

    @GET
    suspend fun getForecastDetails(): Response<ForecastResponse>
}