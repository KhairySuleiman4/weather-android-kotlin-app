package com.example.weatherapp.screens.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weatherapp.R
import com.example.weatherapp.model.locationhelper.LocationHelper
import com.example.weatherapp.model.pojos.Response
import com.example.weatherapp.model.pojos.local.forecast.WeatherForecast
import com.example.weatherapp.model.pojos.local.weather.WeatherDetails
import com.example.weatherapp.model.remote.RemoteDataSourceImp
import com.example.weatherapp.model.remote.RetrofitHelper
import com.example.weatherapp.model.repos.AppRepoImp
import com.example.weatherapp.model.repos.forecasts.ForecastsRepoImp
import com.example.weatherapp.model.repos.location.LocationRepoImp
import com.example.weatherapp.model.repos.settings.SettingsRepoImp
import com.example.weatherapp.model.repos.weather.WeatherRepoImp
import com.example.weatherapp.model.settingshelper.SettingsHelper
import com.example.weatherapp.model.settingshelper.toCelsius
import com.example.weatherapp.model.settingshelper.toFahrenheit
import com.example.weatherapp.model.settingshelper.toMilePerHour
import com.example.weatherapp.model.settingshelper.trimToTwoDecimals
import com.example.weatherapp.ui.theme.Background
import com.example.weatherapp.ui.theme.Day
import com.example.weatherapp.ui.theme.Night
import com.example.weatherapp.ui.theme.Primary
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
        if (bothGranted) {
            viewModel.getCurrentLocation()
            viewModel.getWeatherDetails()
            viewModel.getForecastDetails()
        } else
            Toast.makeText(context, deny, Toast.LENGTH_SHORT).show()
    }

    LaunchedEffect(Unit) {
        viewModel.getStoredSettings()
        if (viewModel.location == "GPS") {
            if (!viewModel.arePermissionsAllowed()) {
                permissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
            } else {
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
        viewModel.toastEvent.collect {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Night)
                .padding(it)
        ) {
            when (val weatherResponse = weatherState.value) {
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
                    WeatherDetailsUI(weatherResponse.data, settings, viewModel.currentTime)
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
            when (val forecastResponse = forecastState.value) {
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
}

@Preview
@Composable
fun PreviewedUI() {
    val context = LocalContext.current
    HomeScreen(
        viewModel(
            factory = HomeFactory(
                AppRepoImp.getInstance(
                    SettingsRepoImp.getInstance(
                        SettingsHelper(context)
                    ),
                    LocationRepoImp.getInstance(
                        LocationHelper(context)
                    ),
                    WeatherRepoImp.getInstance(
                        RemoteDataSourceImp(RetrofitHelper.apiService)
                    ),
                    ForecastsRepoImp.getInstance(
                        RemoteDataSourceImp(RetrofitHelper.apiService)
                    )
                )
            )
        )
    )
}


@Composable
fun WeatherDetailsUI(weatherDetails: WeatherDetails, settings: Map<String, String>, time: String) {
    val context = LocalContext.current
    val temp = getTempWithCurrentUnit(context, weatherDetails.temp, settings["Temperature"])
    val wind = getWindWithCurrentUnit(context, weatherDetails.wind, settings["Wind"])
    val iconRes = getIconNameFromDrawable(context, weatherDetails.icon)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    modifier = Modifier.size(32.dp),
                    painter = painterResource(id = R.drawable.location),
                    contentDescription = stringResource(R.string.location),
                )
                Text(
                    modifier = Modifier.padding(top = 4.dp),
                    text = weatherDetails.cityName,
                    fontSize = 24.sp,
                    color = Color.White
                )
            }
            Text(
                text = time,
                fontSize = 16.sp,
                color = Color.White,
                textAlign = TextAlign.End
            )
        }

        Image(
            modifier = Modifier
                .size(200.dp)
                .padding(start = 40.dp),
            painter = painterResource(id = iconRes),
            contentDescription = stringResource(R.string.status_icon)
        )

        Text(
            modifier = Modifier.padding(top = 4.dp),
            text = temp,
            fontSize = 48.sp,
            color = Color.White
        )

        Text(
            modifier = Modifier.padding(top = 4.dp),
            text = weatherDetails.description,
            fontSize = 32.sp,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .background(
                    color = Background,
                    shape = RoundedCornerShape(24.dp)
                )
                .fillMaxWidth()
                .padding(4.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            WeatherDetailItem(R.drawable.cloud, "${weatherDetails.clouds} %")
            WeatherDetailItem(R.drawable.humidity, "${weatherDetails.humidity} %")
            WeatherDetailItem(R.drawable.wind, wind)
        }
    }
}

@SuppressLint("DiscouragedApi")
fun getIconNameFromDrawable(context: Context, icon: String): Int {
    val formattedIcon = "_" + icon.substring(1)
    return context.resources.getIdentifier(formattedIcon, "drawable", context.packageName)
}

/*Column {
    Text(text = temp.toString(), fontSize = 40.sp)
    Spacer(modifier = Modifier.size(20.dp))
    Text(text = wind.trimToTwoDecimals(), fontSize = 40.sp)
}*/

@Composable
fun ForecastDetailsUI(forecastDetails: List<WeatherForecast>, tempUnit: String) {
    /*Column {
        Text(
            text = "List size:" + forecastDetails.size.toString(),
            fontSize = 40.sp)
        Spacer(modifier = Modifier.size(20.dp))
        Text(text = "Temp: " + getTempWithCurrentUnit(forecastDetails[0].temp.toDouble(), tempUnit).toString(), fontSize = 40.sp)
    }*/
    /*LazyRow {
        items(forecastDetails.size){
            Fore
        }
    }*/
}

@Composable
fun Loading() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
fun WeatherDetailItem(iconRes: Int, value: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.padding(8.dp)
    ) {
        Image(
            modifier = Modifier.size(24.dp),
            painter = painterResource(id = iconRes),
            contentDescription = ""
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = value, fontSize = 16.sp, color = Color.White)
    }
}

fun getWindWithCurrentUnit(context: Context, wind: Double, unit: String?): String {
    return when (unit) {
        context.getString(R.string.mph) -> "${wind.toMilePerHour()} ${context.getString(R.string.mph_unit)}"
        else -> "${wind} ${context.getString(R.string.mps_unit)}"
    }
}

fun getTempWithCurrentUnit(context: Context, temp: Double, unit: String?): String {
    return when (unit) {
        context.getString(R.string.celsius) -> "${temp.toCelsius()} ${context.getString(R.string.celsius_unit)}"
        context.getString(R.string.fahrenheit) -> "${temp.toFahrenheit()} ${context.getString(R.string.fahrenheit_unit)}"
        else -> "${temp.toInt()} ${context.getString(R.string.kelvin_unit)}"
    }
}