package com.example.weatherapp.model.pojos.local.forecast

//need room dependencies
//import androidx.room.Entity
//import androidx.room.PrimaryKey

//@Entity(tableName = "weather_forecast")
data class WeatherForecast(
    //@PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var cityId: Int,    // Foreign Key (linked to WeatherDetails)
    val dt: String,
    var temp: Int,
    val icon: String
)
