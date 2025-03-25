package com.example.weatherapp.screens.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.model.repos.AppRepoImp
import com.example.weatherapp.screens.settings.SettingsViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class HomeViewModel(private val repo: AppRepoImp) : ViewModel() {
    private var lang: String = "English"
    private var temp: String = "Kelvin K"
    private var location: String = "GPS"
    private var wind: String = "m/s"
    private var lat: String = "0.0"
    private var long: String = "0.0"

    fun getStoredSettings() {
        viewModelScope.launch {
            lang = repo.readLanguageChoice().first()
            temp = repo.readTemperatureUnit().first()
            location = repo.readLocationChoice().first()
            wind = repo.readWindSpeedUnit().first()
            lat = repo.readLatLong().first().first
            long = repo.readLatLong().first().second
        }
    }
}

class HomeFactory(private val repo: AppRepoImp) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeViewModel(repo) as T
    }
}