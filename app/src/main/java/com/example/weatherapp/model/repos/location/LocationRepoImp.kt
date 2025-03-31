package com.example.weatherapp.model.repos.location

import com.example.weatherapp.model.locationhelper.LocationHelper

class LocationRepoImp(private val helper: LocationHelper): LocationRepo {
    val lat = helper.lat
    val long = helper.long

    companion object{
        @Volatile
        private var instance: LocationRepoImp? = null

        fun getInstance(helper: LocationHelper): LocationRepoImp {
            if (instance == null) {
                synchronized(this) {
                    if (instance == null) {
                        instance = LocationRepoImp(helper)
                    }
                }
            }
            return instance!!
        }
    }

    override fun getCurrentLocation() = helper.getCurrentLocation()

    override fun arePermissionsAllowed() = helper.arePermissionsAllowed()

    override fun isInternetAvailable() = helper.isInternetAvailable()
}