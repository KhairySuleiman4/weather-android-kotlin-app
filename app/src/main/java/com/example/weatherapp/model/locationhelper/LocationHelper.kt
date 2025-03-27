package com.example.weatherapp.model.locationhelper

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Looper
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.flow.MutableStateFlow

class LocationHelper(private val context: Context) {

    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    var lat: MutableStateFlow<Double?> = MutableStateFlow(null)
    var long: MutableStateFlow<Double?> = MutableStateFlow(null)

    fun arePermissionsAllowed(): Boolean{
        val permissions = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        return (ContextCompat.checkSelfPermission(context, permissions[0]) ==
                PackageManager.PERMISSION_GRANTED
                ||
                ContextCompat.checkSelfPermission(context, permissions[1]) ==
                PackageManager.PERMISSION_GRANTED)
    }

    @SuppressLint("MissingPermission")
    fun getCurrentLocation(){
        if(arePermissionsAllowed()){
            val locationRequest = LocationRequest.Builder(10 * 60 * 1000).apply {
                setPriority(Priority.PRIORITY_HIGH_ACCURACY)
            }.build()

            val locationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    lat.value = locationResult.lastLocation?.latitude
                    long.value = locationResult.lastLocation?.longitude
                }
            }
            fusedLocationClient.requestLocationUpdates(
                locationRequest, locationCallback, Looper.getMainLooper()
            )
        } else {
            Toast.makeText(context, "Location permission not granted", Toast.LENGTH_SHORT).show()
        }
    }
}