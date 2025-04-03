package com.example.weatherapp.screens.favoritedetails

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weatherapp.model.local.WeatherDatabase
import com.example.weatherapp.model.local.forecasts.ForecastsLocalDataSourceImp
import com.example.weatherapp.model.local.weather.WeatherLocalDataSourceImp
import com.example.weatherapp.model.locationhelper.LocationHelper
import com.example.weatherapp.model.pojos.Response
import com.example.weatherapp.model.remote.RemoteDataSourceImp
import com.example.weatherapp.model.remote.RetrofitHelper
import com.example.weatherapp.model.repos.AppRepoImp
import com.example.weatherapp.model.repos.forecasts.ForecastsRepoImp
import com.example.weatherapp.model.repos.location.LocationRepoImp
import com.example.weatherapp.model.repos.settings.SettingsRepoImp
import com.example.weatherapp.model.repos.weather.WeatherRepoImp
import com.example.weatherapp.model.settingshelper.SettingsHelper
import com.example.weatherapp.screens.home.ForecastDetailsUI
import com.example.weatherapp.screens.home.Loading
import com.example.weatherapp.screens.home.WeatherDetailsUI
import com.example.weatherapp.ui.theme.Primary

class FavoriteDetails : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val lat = intent.getDoubleExtra("lat", 0.0)
        val long = intent.getDoubleExtra("long", 0.0)
        val id = intent.getIntExtra("id", 0)
        setContent {
            FavoriteDetailsUI(
                lat = lat,
                long = long,
                id = id,
                viewModel(
                    factory = FavoriteDetailsFactory(
                        AppRepoImp(
                            SettingsRepoImp.getInstance(
                                SettingsHelper(this@FavoriteDetails)
                            ),
                            LocationRepoImp(
                                LocationHelper(this@FavoriteDetails)
                            ),
                            WeatherRepoImp.getInstance(
                                RemoteDataSourceImp(RetrofitHelper.apiService),
                                WeatherLocalDataSourceImp.getInstance(
                                    WeatherDatabase.getInstance(
                                        this@FavoriteDetails
                                    ).weatherDao())
                            ),
                            ForecastsRepoImp.getInstance(
                                RemoteDataSourceImp(RetrofitHelper.apiService),
                                ForecastsLocalDataSourceImp.getInstance(
                                    WeatherDatabase.getInstance(
                                        this@FavoriteDetails
                                    ).weatherDao()
                                )
                            )
                        )
                    )
                )
            )
        }
    }
}

@SuppressLint("StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteDetailsUI(lat: Double, long: Double, id: Int, viewModel: FavoriteDetailsViewModel){
    val weatherState = viewModel.weatherDetails.collectAsState()
    val forecastState = viewModel.forecastDetails.collectAsState()
    val time = viewModel.dateAndTimeToBeDisplayed.collectAsState()
    val isRefreshing = viewModel.isRefreshing.collectAsState()
    val lang = viewModel.lang.collectAsState()
    val temp = viewModel.temp.collectAsState()
    val wind = viewModel.wind.collectAsState()
    val settings = hashMapOf(
        "Language" to lang.value,
        "Temperature" to temp.value,
        "Wind" to wind.value
    )

    LaunchedEffect(Unit) {
        viewModel.getStoredSettings()
        viewModel.getFavoriteDetails(lat, long, id)
    }

    PullToRefreshBox(
        isRefreshing = isRefreshing.value,
        onRefresh = { viewModel.refreshFavDetails(lat, long, id) }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Primary)
        ) {
            when (val weatherResponse = weatherState.value) {
                is Response.Loading -> {
                    item {
                        Loading()
                    }
                }

                is Response.Success -> {
                    item {
                        WeatherDetailsUI(weatherResponse.data, settings, time.value.substring(11, 16))
                    }
                }

                is Response.Failure -> {
                    item {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Weather can't be shown right now",
                                fontSize = 24.sp,
                                color = Color.White
                            )
                        }
                    }
                }
            }
            when (val forecastResponse = forecastState.value) {
                is Response.Loading -> {
                    item {
                        Loading()
                    }
                }

                is Response.Success -> {
                    item {
                        ForecastDetailsUI(
                            forecastResponse.data,
                            temp.value,
                            viewModel.currentDateAndTime.value,
                            viewModel.dateAndTimeToBeDisplayed.value.split(" ")[0]
                        )
                    }
                }

                is Response.Failure -> {
                    item {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Forecast can't be shown right now",
                                fontSize = 24.sp,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}