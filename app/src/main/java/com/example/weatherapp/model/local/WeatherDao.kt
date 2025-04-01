package com.example.weatherapp.model.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.weatherapp.model.pojos.local.forecast.WeatherForecast
import com.example.weatherapp.model.pojos.local.weather.WeatherDetails
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {
    // get
    @Query("Select * from weather_details where isFav = 0")
    fun getWeatherDetailsForHome(): Flow<WeatherDetails>
    @Query("Select * from weather_forecast where isFav = 0")
    fun getForecastsForHome(): Flow<List<WeatherForecast>>
    @Query("Select * from weather_details where isFav = 1")
    fun getFavoriteWeatherDetails(): Flow<List<WeatherDetails>>
    @Query("Select * from weather_forecast where isFav = 1")
    fun getFavoriteForecasts(): Flow<List<WeatherForecast>>
    // insert
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeatherDetails(weather: WeatherDetails)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertForecasts(forecasts: List<WeatherForecast>)
    // delete
    @Query("Delete from weather_details where isFav = 0")
    suspend fun deleteHomeWeatherDetails()
    @Query("Delete from weather_forecast where isFav = 0")
    suspend fun deleteHomeForecasts()
    @Query("Delete from weather_details where cityId = :cityId")
    suspend fun deleteFavoriteCityWeather(cityId: Int)
    @Query("Delete from weather_forecast where cityId = :cityId")
    suspend fun deleteFavoriteCityForecasts(cityId: Int)

    @Transaction
    suspend fun updateHome(weather: WeatherDetails, forecasts: List<WeatherForecast>) {
        deleteHomeWeatherDetails()
        deleteHomeForecasts()

        insertWeatherDetails(weather)
        insertForecasts(forecasts)
    }
}