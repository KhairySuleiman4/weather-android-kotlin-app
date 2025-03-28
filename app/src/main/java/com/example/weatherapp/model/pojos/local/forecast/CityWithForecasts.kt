package com.example.weatherapp.model.pojos.local.forecast

//need room dependencies
//import androidx.room.Embedded
//import androidx.room.Relation
import com.example.weatherapp.model.pojos.local.weather.WeatherDetails
// needed when fetching the city with hourly and daily details from db
data class CityWithForecasts(
    //@Embedded
    val city: WeatherDetails,
    /*@Relation(
        parentColumn = "cityId",
        entityColumn = "cityId"
    )*/
    val forecasts: List<WeatherForecast>
)
