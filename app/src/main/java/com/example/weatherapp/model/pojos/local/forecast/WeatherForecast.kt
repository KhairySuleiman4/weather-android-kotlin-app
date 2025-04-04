package com.example.weatherapp.model.pojos.local.forecast

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather_forecast")
data class WeatherForecast(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var isFav: Boolean,
    var cityId: Int,
    val dt: String,
    var temp: Int,
    val icon: String
)
