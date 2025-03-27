package com.example.weatherapp.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.model.repos.AppRepoImp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class HomeViewModel(private val repo: AppRepoImp) : ViewModel() {
    private var lang: String = "English"
    private var temp: String = "Kelvin K"
    var location: String = "GPS"
    private var wind: String = "m/s"
    private val mutableLat = MutableStateFlow("0.0")
    private val mutableLong = MutableStateFlow("0.0")

    val lat: StateFlow<String> = mutableLat.asStateFlow()
    val long: StateFlow<String> = mutableLong.asStateFlow()

    fun getStoredSettings() = runBlocking {
        lang = repo.readLanguageChoice().first()
        temp = repo.readTemperatureUnit().first()
        location = repo.readLocationChoice().first()
        wind = repo.readWindSpeedUnit().first()
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

    fun getLocationFromDataStore() {
        viewModelScope.launch {
            mutableLat.value = repo.readLatLong().first().first
            mutableLong.value = repo.readLatLong().first().second
        }
    }
}

class HomeFactory(private val repo: AppRepoImp) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeViewModel(repo) as T
    }
}