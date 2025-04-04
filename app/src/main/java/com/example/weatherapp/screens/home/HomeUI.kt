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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherapp.R
import com.example.weatherapp.model.pojos.Response
import com.example.weatherapp.model.pojos.local.forecast.WeatherForecast
import com.example.weatherapp.model.pojos.local.weather.WeatherDetails
import com.example.weatherapp.model.settingshelper.formatDateBasedOnLocale
import com.example.weatherapp.model.settingshelper.formatNumber
import com.example.weatherapp.model.settingshelper.toCelsius
import com.example.weatherapp.model.settingshelper.toFahrenheit
import com.example.weatherapp.model.settingshelper.toMilePerHour
import com.example.weatherapp.model.settingshelper.translateWeatherDescription
import com.example.weatherapp.ui.theme.Background
import com.example.weatherapp.ui.theme.Night
import com.example.weatherapp.ui.theme.Primary
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@SuppressLint("StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: HomeViewModel) {
    val context = LocalContext.current
    val deny = stringResource(R.string.permission_denied)
    val weatherState = viewModel.weatherDetails.collectAsState()
    val forecastState = viewModel.forecastDetails.collectAsState()
    val time = viewModel.dateAndTimeToBeDisplayed.collectAsState()
    val isRefreshing = viewModel.isRefreshing.collectAsState()
    val settings = hashMapOf(
        "Language" to viewModel.lang,
        "Temperature" to viewModel.temp,
        "Location" to viewModel.location,
        "Wind" to viewModel.wind
    )

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
    ) { permissions ->
        val bothGranted = permissions.all { it.value }
        if (bothGranted) {
            viewModel.getCurrentLocation()
            viewModel.getWeatherAndForecastDetails()
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
                viewModel.getWeatherAndForecastDetails()
            }
        } else {
            viewModel.getLocationFromDataStore()
            viewModel.getWeatherAndForecastDetails()
        }
    }

    LaunchedEffect(viewModel.toastEvent) {
        viewModel.toastEvent.collect {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    }

    PullToRefreshBox(
        isRefreshing = isRefreshing.value,
        onRefresh = { viewModel.refreshHome() }
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
                            viewModel.temp,
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

@Composable
fun WeatherDetailsUI(weatherDetails: WeatherDetails, settings: Map<String, String>, time: String) {
    val context = LocalContext.current
    val temp = getTempWithCurrentUnit(context, weatherDetails.temp, settings["Temperature"])
    val wind = getWindWithCurrentUnit(context, weatherDetails.wind, settings["Wind"])
    val iconRes = getIconNameFromDrawable(context, weatherDetails.icon)

    Column(
        modifier = Modifier
            .fillMaxSize()
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
            text = "${temp.first} ${temp.second}",
            fontSize = 48.sp,
            color = Color.White
        )

        Text(
            modifier = Modifier.padding(top = 4.dp),
            text = translateWeatherDescription(weatherDetails.description),
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
            WeatherDetailItem(R.drawable.cloud, formatNumber(weatherDetails.clouds) to "%")
            WeatherDetailItem(R.drawable.humidity, formatNumber(weatherDetails.humidity) to "%")
            WeatherDetailItem(R.drawable.wind, wind)
        }
    }
}

@Composable
fun ForecastDetailsUI(
    forecastDetails: List<WeatherForecast>,
    tempUnit: String,
    dateAndTime: String,
    dateDisplayed: String
) {
    val context = LocalContext.current
    val hourlyList = mutableListOf<WeatherForecast>()
    val dailyList = mutableListOf<WeatherForecast>()
    val todayInNumbers = dateAndTime.split(" ")[0]
    for (forecast in forecastDetails) {
        if (forecast.dt.split(" ")[0] == todayInNumbers)
            hourlyList.add(forecast)
        else
            dailyList.add(forecast)
    }
    val averages = getTemperaturesOfFiveDays(context, tempUnit, dailyList)
    val icons = getIconsOfFiveDays(dailyList)
    val nextFiveDays = getTheNextFiveDaysAsStrings(todayInNumbers)
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            Card(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(Background)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.today),
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(text = dateDisplayed, color = Color.White, fontSize = 16.sp)
                    }
                    LazyRow {
                        items(hourlyList.size) {
                            HourlyColumn(hourlyList[it], tempUnit)
                        }
                    }
                }
            }

            Card(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(Background)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.five_day_forecast),
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Image(
                            modifier = Modifier
                                .size(24.dp),
                            painter = painterResource(R.drawable.calendar),
                            contentDescription = null
                        )
                    }
                    Column {
                        for (i in averages.indices) {
                            DailyRow(averages[i], nextFiveDays[i], icons[i])
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun WeatherDetailItem(iconRes: Int, value: Pair<String, String>) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.padding(8.dp)
    ) {
        Image(
            modifier = Modifier.size(32.dp),
            painter = painterResource(id = iconRes),
            contentDescription = ""
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "${value.first} ${value.second}",
            fontSize = 16.sp,
            color = Color.White
        )
    }
}

@Composable
fun Loading() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
fun HourlyColumn(forecast: WeatherForecast, tempUnit: String) {
    val context = LocalContext.current
    val iconRes = getIconNameFromDrawable(context, forecast.icon)
    val temp = getTempWithCurrentUnit(context, forecast.temp.toDouble(), tempUnit)
    val time = formatDateBasedOnLocale(forecast.dt.split(" ")[1].substring(0, 5))

    Column(
        modifier = Modifier
            .padding(8.dp)
            .width(64.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = time,
            color = Color.White,
            fontSize = 14.sp
        )
        Spacer(modifier = Modifier.height(4.dp))
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = null,
            modifier = Modifier.size(32.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "${temp.first} ${temp.second}",
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun DailyRow(tempAverage: Pair<String, String>, day: Pair<String, String>, icon: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "${day.first}\n${day.second}",
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f)
        )
        Image(
            painter = painterResource(id = icon),
            contentDescription = null,
            modifier = Modifier.size(32.dp)
        )
        Text(
            text = "${tempAverage.first} ${tempAverage.second}",
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}

@SuppressLint("DiscouragedApi")
fun getIconNameFromDrawable(context: Context, icon: String): Int {
    val formattedIcon = "_$icon"
    return context.resources.getIdentifier(formattedIcon, "drawable", context.packageName)
}

fun getWindWithCurrentUnit(context: Context, wind: Double, unit: String?): Pair<String, String> {
    return when (unit) {
        context.getString(R.string.mph) -> formatNumber(wind.toMilePerHour()) to context.getString(R.string.mph_unit)

        else -> formatNumber(wind) to context.getString(R.string.mps_unit)
    }
}

fun getTempWithCurrentUnit(context: Context, temp: Double, unit: String?): Pair<String, String> {
    return when (unit) {
        context.getString(R.string.celsius) -> formatNumber(temp.toCelsius().toInt()) to
                context.getString(R.string.celsius_unit)

        context.getString(R.string.fahrenheit) -> formatNumber(temp.toFahrenheit().toInt()) to context.getString(R.string.fahrenheit_unit)

        else -> formatNumber(temp.toInt()) to context.getString(R.string.kelvin_unit)
    }
}

fun getTemperaturesOfFiveDays(
    context: Context,
    unit: String,
    dailyList: MutableList<WeatherForecast>
): List<Pair<String, String>> {
    val listOfDoubleAverages = dailyList
        .groupBy { it.dt.substring(0, 10) }
        .values
        .map { forecasts ->
            forecasts.map {
                it.temp
            }.average()
        }
    val listOfPairs = mutableListOf<Pair<String, String>>()
    for (average in listOfDoubleAverages) {
        listOfPairs.add(getTempWithCurrentUnit(context, average, unit))
    }
    return listOfPairs
}

fun getIconsOfFiveDays(dailyList: MutableList<WeatherForecast>): List<Int> {
    return dailyList
        .groupBy { it.dt.substring(0, 10) }
        .values
        .map { dayList ->
            val mostFrequentIcon = dayList.groupingBy { it.icon }
                .eachCount()
                .maxByOrNull { it.value }?.key ?: ""
            mapIconToResourceId(mostFrequentIcon)
        }
}

fun mapIconToResourceId(iconName: String): Int {
    return when (iconName) {
        "01d" -> R.drawable._01d
        "01n" -> R.drawable._01n
        "02d" -> R.drawable._02d
        "02n" -> R.drawable._02n
        "03d" -> R.drawable._03d
        "03n" -> R.drawable._03n
        "04d" -> R.drawable._04d
        "04n" -> R.drawable._04n
        "09d" -> R.drawable._09d
        "09n" -> R.drawable._09n
        "10d" -> R.drawable._10d
        "10n" -> R.drawable._10n
        "11d" -> R.drawable._11d
        "11n" -> R.drawable._11n
        "13d" -> R.drawable._13d
        "13n" -> R.drawable._13n
        "50d" -> R.drawable._50d
        "50n" -> R.drawable._50n
        else -> R.drawable._01n
    }
}

fun getTheNextFiveDaysAsStrings(todayInNumbers: String): List<Pair<String, String>> {
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val dayFormat = SimpleDateFormat("EEEE", Locale.getDefault())

    val calendar = Calendar.getInstance()
    calendar.time = sdf.parse(todayInNumbers)!!

    return List(5) {
        calendar.add(Calendar.DAY_OF_YEAR, 1)
        val date = sdf.format(calendar.time)
        val dayName = dayFormat.format(calendar.time)
        dayName to date
    }
}