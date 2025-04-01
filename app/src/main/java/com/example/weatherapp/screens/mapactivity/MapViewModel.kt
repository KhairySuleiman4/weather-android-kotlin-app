package com.example.weatherapp.screens.mapactivity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.model.repos.AppRepoImp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MapViewModel(private val repo: AppRepoImp): ViewModel() {
    fun saveCoordinatesToDataStore(lat: Double, long: Double){
        viewModelScope.launch {
            repo.writeLatLong(lat, long)
        }
    }

    fun getWeatherAndForecastForFavoriteAndInsertThemToDatabase(lat: Double, long: Double){
        viewModelScope.launch(Dispatchers.IO) {
            repo.getWeatherDetails(lat, long)
                .collect{
                    it.isFav = true
                    repo.insertWeatherDetailsToDatabase(it)
                }
            repo.getForecastDetails(lat, long)
                .collect{
                    it.forEach{ forecast ->
                        forecast.isFav = true
                    }
                    repo.insertForecastsToDatabase(it)
                }
        }
    }
}

class MapFactory(private val repo: AppRepoImp): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MapViewModel(repo) as T
    }
}