package com.example.weatherapp.model.local

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.weatherapp.model.pojos.local.weather.WeatherDetails
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.After
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@SmallTest
@RunWith(AndroidJUnit4::class)
class WeatherDaoTest {
    private lateinit var dao: WeatherDao
    private lateinit var database: WeatherDatabase

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            WeatherDatabase::class.java
        ).build()
        dao = database.weatherDao()
    }

    @Test
    fun getFavoriteWeatherDetails_DummyWeatherDetailsObject_getTheInsertedObject() = runTest {
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
        dao.insertWeatherDetails(weatherDetails)
        // When -> call the getFavoriteWeatherDetails(cityId)
        val result = dao.getFavoriteWeatherDetails(0).first()
        // Then -> ensure that the returned object is the same inserted one
        assertNotNull(result)
        assertThat(result.temp, `is`(result.temp))
        assertThat(result.cityName, `is`(result.cityName))
        assertThat(result.clouds, `is`(result.clouds))
    }

    @Test
    fun getFavoriteWeatherDetails_DummyWeatherDetailsObject_deleteTheInsertedThenGetNull() = runTest {
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
        dao.insertWeatherDetails(weatherDetails)
        dao.deleteFavoriteCityWeather(weatherDetails.cityId)
        // When -> call the getFavoriteWeatherDetails(cityId)
        val result = dao.getFavoriteWeatherDetails(0).first()
        // Then -> ensure that the result is null (the object is deleted)
        assertNull(result)
    }

    @After
    fun tearDown() = database.close()
}