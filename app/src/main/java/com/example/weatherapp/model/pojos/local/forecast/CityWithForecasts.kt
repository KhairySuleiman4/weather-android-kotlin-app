package com.example.weatherapp.model.pojos.local.forecast

//need room dependencies
//import androidx.room.Embedded
//import androidx.room.Relation
import com.example.weatherapp.model.pojos.local.weather.WeatherDetails

data class CityWithForecasts(
    //@Embedded
    val city: WeatherDetails,
    /*@Relation(
        parentColumn = "cityId",
        entityColumn = "cityId"
    )*/
    val forecasts: List<WeatherForecast>
)
