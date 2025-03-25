package com.example.weatherapp.screens.mapactivity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.model.repos.AppRepoImp
import kotlinx.coroutines.launch

class MapViewModel(private val repo: AppRepoImp): ViewModel() {
    fun saveCoordinatesToDataStore(lat: Double, long: Double){
        viewModelScope.launch {
            repo.writeLatLong(lat, long)
        }
    }
}

class MapFactory(private val repo: AppRepoImp): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MapViewModel(repo) as T
    }
}