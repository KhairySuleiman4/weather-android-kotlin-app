package com.example.weatherapp.model.local.weather

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.example.weatherapp.model.local.WeatherDao
import com.example.weatherapp.model.local.WeatherDatabase
import com.example.weatherapp.model.pojos.local.weather.WeatherDetails
import kotlinx.coroutines.flow.first
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@MediumTest
@RunWith(AndroidJUnit4::class)
class WeatherLocalDataSourceImpTest {
    private lateinit var database: WeatherDatabase
    private lateinit var dao: WeatherDao
    private lateinit var localDataSource: WeatherLocalDataSourceImp

    @Before
    fun setUp(){
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            WeatherDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = database.weatherDao()
        localDataSource = WeatherLocalDataSourceImp.getInstance(dao)
    }

    @Test
    fun insertWeatherDetails_DummyWeatherDetailsObject_getTheInsertedObject() = runTest{
        // Given -> dummy weatherDetails object
        val weatherDetails = WeatherDetails(
            cityId = 0,
            isFav = true,
            lastUpdate = "0",
            icon = "0",
            temp = 12.0,
            wind = 3.0,
            description = "clear sky",
            lat = 35.5,
            lon = 30.5,
            clouds = 50,
            cityName = "Ismailiah",
            humidity = 70,
            pressure = 10
        )
        localDataSource.insertWeatherDetails(weatherDetails)
        // When -> get the inserted object
        val result = localDataSource.getFavoriteWeatherDetails(weatherDetails.cityId).first()
        // Then -> ensure that the result is the actual inserted object
        assertNotNull(result)
        assertThat(result.temp, `is`(result.temp))
        assertThat(result.cityName, `is`(result.cityName))
        assertThat(result.clouds, `is`(result.clouds))
    }

    @Test
    fun deleteFavoriteCityWeather_DummyWeatherDetailsObject_deleteTheInsertedThenGetNull() = runTest{
        // Given -> dummy weatherDetails object
        val weatherDetails = WeatherDetails(
            cityId = 0,
            isFav = true,
            lastUpdate = "0",
            icon = "0",
            temp = 12.0,
            wind = 3.0,
            description = "clear sky",
            lat = 35.5,
            lon = 30.5,
            clouds = 50,
            cityName = "Ismailiah",
            humidity = 70,
            pressure = 10
        )
        localDataSource.insertWeatherDetails(weatherDetails)
        localDataSource.deleteFavoriteCityWeather(weatherDetails.cityId)
        // When -> get the inserted object
        val result = localDataSource.getFavoriteWeatherDetails(weatherDetails.cityId).first()
        // Then -> ensure that the result is null (the object is deleted)
        assertNull(result)
    }
}