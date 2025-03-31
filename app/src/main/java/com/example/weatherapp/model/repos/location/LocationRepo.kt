package com.example.weatherapp.model.repos.location

interface LocationRepo {
    fun getCurrentLocation()
    fun arePermissionsAllowed(): Boolean
    fun isInternetAvailable(): Boolean
}