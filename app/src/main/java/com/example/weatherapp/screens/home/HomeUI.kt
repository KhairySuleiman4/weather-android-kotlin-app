package com.example.weatherapp.screens.home

import android.Manifest
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherapp.R
import com.example.weatherapp.model.pojos.Response
import com.example.weatherapp.model.pojos.local.forecast.WeatherForecast
import com.example.weatherapp.model.pojos.local.weather.WeatherDetails
import com.example.weatherapp.model.settingshelper.toCelsius
import com.example.weatherapp.model.settingshelper.toFahrenheit
import com.example.weatherapp.model.settingshelper.toMilePerHour
import com.example.weatherapp.model.settingshelper.trimToTwoDecimals
import kotlinx.coroutines.delay

@Composable
fun HomeScreen(viewModel: HomeViewModel) {
    val context = LocalContext.current
    val deny = stringResource(R.string.permission_denied)
    val weatherState = viewModel.weatherDetails.collectAsState()
    val forecastState = viewModel.forecastDetails.collectAsState()

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
    ) { permissions ->
        val bothGranted = permissions.all { it.value }
        if(bothGranted){
            viewModel.getCurrentLocation()
            viewModel.getWeatherDetails()
            viewModel.getForecastDetails()
        }
        else
            Toast.makeText(context, deny, Toast.LENGTH_SHORT).show()
    }

    LaunchedEffect(Unit) {
        viewModel.getStoredSettings()
        if(viewModel.location == "GPS"){
            if (!viewModel.arePermissionsAllowed()) {
                permissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
            } else{
                viewModel.getCurrentLocation()
                viewModel.getWeatherDetails()
                viewModel.getForecastDetails()
            }
        } else {
            viewModel.getLocationFromDataStore()
            viewModel.getWeatherDetails()
            viewModel.getForecastDetails()
        }
    }

    LaunchedEffect(viewModel.toastEvent) {
        viewModel.toastEvent.collect{
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        when(val weatherResponse = weatherState.value){
            is Response.Loading -> {
                Loading()
            }
            is Response.Success -> {
                val settings = hashMapOf(
                    "Language" to viewModel.lang,
                    "Temperature" to viewModel.temp,
                    "Location" to viewModel.location,
                    "Wind" to viewModel.wind
                )
                WeatherDetailsUI(weatherResponse.data, settings)
            }
            is Response.Failure -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = "Weather can't be shown right now",
                        fontSize = 24.sp
                    )
                }
            }
        }
        when(val forecastResponse = forecastState.value){
            is Response.Loading -> {
                Loading()
            }
            is Response.Success -> {
                ForecastDetailsUI(forecastResponse.data, viewModel.temp)
            }
            is Response.Failure -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = "Forecast can't be shown right now",
                        fontSize = 24.sp
                    )
                }
            }
        }
    }
}

@Composable
fun WeatherDetailsUI(weatherDetails: WeatherDetails, settings: Map<String, String>){
    val temp = getTempWithCurrentUnit(weatherDetails.temp, settings["Temperature"])
    val wind = getWindWithCurrentUnit(weatherDetails.wind, settings["Wind"])

    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        Column {
            Text(text = temp.toString(), fontSize = 40.sp)
            Spacer(modifier = Modifier.size(20.dp))
            Text(text = wind.trimToTwoDecimals(), fontSize = 40.sp)
        }
    }
}

@Composable
fun ForecastDetailsUI(forecastDetails: List<WeatherForecast>, tempUnit: String){
    Column {
        Text(
            text = "List size:" + forecastDetails.size.toString(),
            fontSize = 40.sp)
        Spacer(modifier = Modifier.size(20.dp))
        Text(text = "Temp: " + getTempWithCurrentUnit(forecastDetails[0].temp.toDouble(), tempUnit).toString(), fontSize = 40.sp)
    }
    /*LazyRow {
        items(forecastDetails.size){
            Fore
        }
    }*/
}

@Composable
fun Loading(){
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

fun getWindWithCurrentUnit(wind: Double, unit: String?): Double{
    return when (unit) {
        "Mile/Hour" -> wind.toMilePerHour()
        else -> wind
    }
}

fun getTempWithCurrentUnit(temp: Double, unit: String?): Int{
    return when (unit) {
        "Celsius °C" -> temp.toCelsius()
        "Fahrenheit °F" -> temp.toFahrenheit()
        else -> temp.toInt()
    }
}