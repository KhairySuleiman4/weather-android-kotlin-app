package com.example.weatherapp.screens.home

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.model.pojos.Response
import com.example.weatherapp.model.pojos.local.forecast.WeatherForecast
import com.example.weatherapp.model.pojos.local.weather.WeatherDetails
import com.example.weatherapp.model.repos.AppRepoImp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HomeViewModel(private val repo: AppRepoImp) : ViewModel() {
    private val mutableLang = MutableStateFlow("English")
    val lang = mutableLang.asStateFlow()
    private val mutableTemp = MutableStateFlow("Kelvin K")
    val temp = mutableTemp.asStateFlow()
    private val mutableLocation = MutableStateFlow("GPS")
    val location = mutableLocation.asStateFlow()
    private val mutableWind = MutableStateFlow("m/s")
    val wind = mutableWind.asStateFlow()

    private val mutableLat = MutableStateFlow("0.0")
    val lat: StateFlow<String> = mutableLat.asStateFlow()

    private val mutableLong = MutableStateFlow("0.0")
    val long: StateFlow<String> = mutableLong.asStateFlow()

    private val mutableWeatherDetails = MutableStateFlow<Response<WeatherDetails>>(Response.Loading)
    val weatherDetails = mutableWeatherDetails.asStateFlow()

    private val mutableForecastDetails = MutableStateFlow<Response<List<WeatherForecast>>>(Response.Loading)
    val forecastDetails = mutableForecastDetails.asStateFlow()

    private val mutableToastEvent = MutableSharedFlow<String>()
    val toastEvent = mutableToastEvent.asSharedFlow()

    private val mutableIsRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = mutableIsRefreshing

    @SuppressLint("SimpleDateFormat")
    private val mutableCurrentDateAndTime = MutableStateFlow(SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).format(Date()))
    val currentDateAndTime = mutableCurrentDateAndTime
    private val mutableDateAndTimeToBeDisplayed = MutableStateFlow(SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date()))
    val dateAndTimeToBeDisplayed = mutableDateAndTimeToBeDisplayed

    fun refreshHome(){
        viewModelScope.launch {
            mutableIsRefreshing.value = true
            getWeatherAndForecastDetails()
            mutableIsRefreshing.value = false
        }
    }

    fun getStoredSettings() = runBlocking {
        mutableLang.value = repo.readLanguageChoice().first()
        mutableTemp.value = repo.readTemperatureUnit().first()
        mutableLocation.value = repo.readLocationChoice().first()
        mutableWind.value = repo.readWindSpeedUnit().first()
    }

    fun getCurrentLocation() {
        repo.getUserLocation()
        viewModelScope.launch {
            val repoLat = repo.lat.filterNotNull().first()
            val repoLong = repo.long.filterNotNull().first()

            mutableLat.value = repoLat.toString()
            mutableLong.value = repoLong.toString()
        }
    }

    fun arePermissionsAllowed() = repo.areLocationPermissionsGranted()

    fun getLocationFromDataStore() = runBlocking{
        mutableLat.value = repo.readLatLong().first().first
        mutableLong.value = repo.readLatLong().first().second
    }

    fun getWeatherAndForecastDetails() {
        viewModelScope.launch(Dispatchers.IO) {
            if(repo.isInternetAvailable()) {
                try {
                    val weatherData = repo.getWeatherDetails(lat.value.toDouble(), long.value.toDouble()).first()
                    mutableWeatherDetails.value = Response.Success(weatherData)

                    val forecastData = repo.getForecastDetails(lat.value.toDouble(), long.value.toDouble()).first()
                    mutableForecastDetails.value = Response.Success(forecastData)

                    repo.updateHome(weatherData, forecastData)
                } catch (ex: Exception) {
                    mutableWeatherDetails.value = Response.Failure(ex)
                    mutableForecastDetails.value = Response.Failure(ex)
                    mutableToastEvent.emit("Error: ${ex.message}")
                }
            } else {
                viewModelScope.launch {
                    getWeatherDetailsFromDatabase()
                }
                getForecastDetailsFromDatabase()
            }
        }
    }

    private suspend fun getWeatherDetailsFromDatabase(){
        repo.getWeatherDetailsForHome()
            .catch { ex ->
                mutableWeatherDetails.value = Response.Failure(ex)
                mutableToastEvent.emit("Error from API: ${ex.message}")
            }
            .collect{ mutableWeatherDetails.value = Response.Success(it) }
    }

    private suspend fun getForecastDetailsFromDatabase(){
        repo.getForecastsForHome()
            .catch { ex ->
                mutableForecastDetails.value = Response.Failure(ex)
                mutableToastEvent.emit("Error from API: ${ex.message}")
            }
            .collect{ mutableForecastDetails.value = Response.Success(it) }
    }
}

class HomeFactory(private val repo: AppRepoImp) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeViewModel(repo) as T
    }
}