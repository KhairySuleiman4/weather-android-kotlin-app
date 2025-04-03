package com.example.weatherapp.model.repos.weather

import com.example.weatherapp.model.local.weather.FakeWeatherLocalDataSourceImp
import com.example.weatherapp.model.local.weather.WeatherLocalDataSource
import com.example.weatherapp.model.pojos.local.weather.WeatherDetails
import com.example.weatherapp.model.remote.FakeRemoteDataSourceImp
import com.example.weatherapp.model.remote.RemoteDataSource
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.core.IsEqual
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test


class WeatherRepoImpTest{
    // repo(remote, local) -> using FakeRemoteDataSource and FakeLocalDataSource

    private val remoteWeatherDetails = mutableListOf(
        WeatherDetails(
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
            cityName = "Ismailia",
            humidity = 70,
            pressure = 10
        ),
        WeatherDetails(
            cityId = 1,
            isFav = true,
            lastUpdate = "0",
            icon = "0",
            temp = 12.0,
            wind = 3.0,
            description = "clear sky",
            lat = 36.0,
            lon = 33.5,
            clouds = 50,
            cityName = "Cairo",
            humidity = 70,
            pressure = 10
        )
    )

    private val localWeatherDetails = mutableListOf(
        WeatherDetails(
            cityId = 2,
            isFav = true,
            lastUpdate = "0",
            icon = "0",
            temp = 12.0,
            wind = 3.0,
            description = "clear sky",
            lat = 31.5,
            lon = 35.5,
            clouds = 50,
            cityName = "Belbeis",
            humidity = 70,
            pressure = 10
        ),
        WeatherDetails(
            cityId = 3,
            isFav = true,
            lastUpdate = "0",
            icon = "0",
            temp = 12.0,
            wind = 3.0,
            description = "clear sky",
            lat = 30.5,
            lon = 36.5,
            clouds = 50,
            cityName = "Zagazig",
            humidity = 70,
            pressure = 10
        )
    )

    private lateinit var fakeRemoteDataSource: RemoteDataSource
    private lateinit var fakeLocalDataSource: WeatherLocalDataSource
    private lateinit var repo: WeatherRepoImp

    @Before
    fun setUp(){
        // Given -> Fake data sources carrying dummy data
        fakeRemoteDataSource = FakeRemoteDataSourceImp(remoteWeatherDetails)
        fakeLocalDataSource = FakeWeatherLocalDataSourceImp(localWeatherDetails)
        repo = WeatherRepoImp(fakeRemoteDataSource, fakeLocalDataSource)
    }

    @Test
    fun getWeatherDetails_latAndLong_returnWeatherDetails() = runTest{
        // When -> call the function giving it existing properties in fake data sources
        val result = repo.getWeatherDetails(35.5, 30.5).first()
        // Then -> ensure that the weatherDetails object exists in remote data source
        assertThat(result, `is`(remoteWeatherDetails[0]))
    }

    @Test
    fun getWeatherDetails_latAndLong_returnWeatherDetailsNotFoundException() = runTest{
        try {
            repo.getWeatherDetails(0.0, 0.0).first()
            fail("Expected an Exception to be thrown, but it didn't")
        } catch (e: Exception) {
            assertThat(e.message, IsEqual.equalTo("Weather Details not Found!"))
        }
    }
}