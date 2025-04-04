package com.example.weatherapp.screens.favoritedetails

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.model.pojos.Response
import com.example.weatherapp.model.pojos.local.forecast.WeatherForecast
import com.example.weatherapp.model.pojos.local.weather.WeatherDetails
import com.example.weatherapp.model.repos.AppRepoImp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class FavoriteDetailsViewModel(private val repo: AppRepoImp): ViewModel() {
    private val mutableLanguage = MutableStateFlow("English")
    val lang = mutableLanguage.asStateFlow()
    private val mutableTemp = MutableStateFlow("Kelvin K")
    val temp = mutableTemp.asStateFlow()
    private val mutableWind = MutableStateFlow("m/s")
    val wind = mutableWind.asStateFlow()

    private val mutableWeatherDetails = MutableStateFlow<Response<WeatherDetails>>(Response.Loading)
    val weatherDetails = mutableWeatherDetails.asStateFlow()

    private val mutableForecastDetails = MutableStateFlow<Response<List<WeatherForecast>>>(Response.Loading)
    val forecastDetails = mutableForecastDetails.asStateFlow()

    private val mutableIsRefreshing = MutableStateFlow(false)
    val isRefreshing = mutableIsRefreshing.asStateFlow()

    @SuppressLint("SimpleDateFormat")
    private val mutableCurrentDateAndTime = MutableStateFlow(
        SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).format(
            Date()
        ))
    val currentDateAndTime = mutableCurrentDateAndTime
    private val mutableDateAndTimeToBeDisplayed = MutableStateFlow(
        SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(
            Date()
        ))
    val dateAndTimeToBeDisplayed = mutableDateAndTimeToBeDisplayed

    fun getStoredSettings() = runBlocking {
        mutableLanguage.value = repo.readLanguageChoice().first()
        mutableTemp.value = repo.readTemperatureUnit().first()
        mutableWind.value = repo.readWindSpeedUnit().first()
    }

    fun getFavoriteDetails(lat: Double, long: Double, id: Int){
        viewModelScope.launch(Dispatchers.IO) {
            if(repo.isInternetAvailable()){
                repo.getWeatherDetails(lat, long)
                    .collect{
                        it.isFav = true
                        mutableWeatherDetails.value = Response.Success(it)
                        repo.insertWeatherDetailsToDatabase(it)
                    }
                repo.getForecastDetails(lat, long)
                    .collect{
                        it.forEach{ forecast ->
                            forecast.isFav = true
                        }
                        mutableForecastDetails.value = Response.Success(it)
                        repo.insertForecastsToDatabase(it)
                    }
            } else{
                viewModelScope.launch(Dispatchers.IO) {
                    repo.getFavoriteWeatherDetails(id)
                        .collect{
                            mutableWeatherDetails.value = Response.Success(it)
                        }
                }
                repo.getFavoriteForecasts(id)
                    .collect{
                        mutableForecastDetails.value = Response.Success(it)
                    }
            }
        }
    }

    fun refreshFavDetails(lat: Double, long: Double, id: Int){
        viewModelScope.launch {
            mutableIsRefreshing.value = true
            getFavoriteDetails(lat, long, id)
            mutableIsRefreshing.value = false
        }
    }
}

class FavoriteDetailsFactory(private val repo: AppRepoImp) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return FavoriteDetailsViewModel(repo) as T
    }
}