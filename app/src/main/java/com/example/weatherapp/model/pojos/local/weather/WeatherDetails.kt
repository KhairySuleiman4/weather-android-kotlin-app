package com.example.weatherapp.model.pojos.local.weather

//needs room dependencies
//import androidx.room.Entity
//import androidx.room.PrimaryKey

//@Entity(tableName = "weather_details")
data class WeatherDetails(
    //@PrimaryKey
    val cityId: Int,
    val isFav: Boolean,
    val lon: Double,
    val lat: Double,
    val temp: Double,
    val humidity: Int,
    val wind: Double,
    val pressure: Int,
    val clouds: Int,
    val cityName: String,
    //val country: String,
    val icon: String,
    val description: String
)
