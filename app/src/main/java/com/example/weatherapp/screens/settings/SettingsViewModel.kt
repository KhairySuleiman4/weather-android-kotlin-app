package com.example.weatherapp.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.model.repos.AppRepoImp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SettingsViewModel(private val repo: AppRepoImp) : ViewModel(){
    private val mutableLanguage = MutableStateFlow("English")
    val language = mutableLanguage.asStateFlow()
    private val mutableTemp = MutableStateFlow("Kelvin K")
    val temp = mutableTemp.asStateFlow()
    private val mutableLocation = MutableStateFlow("GPS")
    val location = mutableLocation.asStateFlow()
    private val mutableWind = MutableStateFlow("m/s")
    val wind = mutableWind.asStateFlow()
    private val mutableIsConnected = MutableStateFlow(false)
    val isConnected = mutableIsConnected.asStateFlow()

    init {
        isConnectedToInternet()
    }

    fun saveSettings(language: String, temp: String, location: String, wind: String){
        viewModelScope.launch {
            repo.writeLanguageChoice(language)
            repo.writeTemperatureUnit(temp)
            repo.writeLocationChoice(location)
            repo.writeWindSpeedUnit(wind)

            mutableLanguage.value = language
            mutableTemp.value = temp
            mutableLocation.value = location
            mutableWind.value = wind
        }
    }

    fun getSavedSettings(){
        viewModelScope.launch {
            repo.readLanguageChoice().collect {
                mutableLanguage.value = it
            }
        }
        viewModelScope.launch {
            repo.readTemperatureUnit().collect {
                mutableTemp.value = it
            }
        }
        viewModelScope.launch {
            repo.readLocationChoice().collect {
                mutableLocation.value = it
            }
        }
        viewModelScope.launch {
            repo.readWindSpeedUnit().collect {
                mutableWind.value = it
            }
        }
    }

    fun isConnectedToInternet(){
        mutableIsConnected.value = repo.isInternetAvailable()
    }
}

class SettingsFactory(private val repo: AppRepoImp): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SettingsViewModel(repo) as T
    }
}