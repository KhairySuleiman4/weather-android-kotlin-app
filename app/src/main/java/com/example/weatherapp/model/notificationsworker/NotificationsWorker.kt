package com.example.weatherapp.model.notificationsworker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.weatherapp.R
import com.example.weatherapp.model.local.WeatherDatabase
import com.example.weatherapp.model.local.forecasts.ForecastsLocalDataSourceImp
import com.example.weatherapp.model.local.weather.WeatherLocalDataSourceImp
import com.example.weatherapp.model.locationhelper.LocationHelper
import com.example.weatherapp.model.pojos.local.weather.WeatherDetails
import com.example.weatherapp.model.remote.RemoteDataSourceImp
import com.example.weatherapp.model.remote.RetrofitHelper
import com.example.weatherapp.model.repos.AppRepoImp
import com.example.weatherapp.model.repos.forecasts.ForecastsRepoImp
import com.example.weatherapp.model.repos.location.LocationRepoImp
import com.example.weatherapp.model.repos.settings.SettingsRepoImp
import com.example.weatherapp.model.repos.weather.WeatherRepoImp
import com.example.weatherapp.model.settingshelper.SettingsHelper
import com.example.weatherapp.screens.home.mapIconToResourceId
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withTimeoutOrNull

class NotificationsWorker(private val context: Context, workerParams: WorkerParameters): CoroutineWorker(context, workerParams) {
    companion object {
        const val CHANNEL_ID = "weather_channel"
        const val CHANNEL_NAME = "Weather Updates"
        const val CHANNEL_DESCRIPTION = "Notifications for weather updates"
        const val NOTIFICATION_ID = 1
        const val WEATHER_FETCH_TIMEOUT = 30000L
    }

    private val repo = AppRepoImp.getInstance(
        SettingsRepoImp.getInstance(
            SettingsHelper(context)
        ),
        LocationRepoImp.getInstance(
            LocationHelper(context)
        ),
        WeatherRepoImp.getInstance(
            RemoteDataSourceImp(RetrofitHelper.apiService),
            WeatherLocalDataSourceImp.getInstance(
                WeatherDatabase.getInstance(
                    context
                ).weatherDao())
        ),
        ForecastsRepoImp.getInstance(
            RemoteDataSourceImp(RetrofitHelper.apiService),
            ForecastsLocalDataSourceImp.getInstance(
                WeatherDatabase.getInstance(
                    context
                ).weatherDao()
            )
        )
    )

    override suspend fun doWork(): Result {
        try {
            createNotificationChannel()
            val lat = repo.lat.filterNotNull().first()
            val long = repo.long.filterNotNull().first()

            val weatherDetails = withTimeoutOrNull(WEATHER_FETCH_TIMEOUT){
                repo.getWeatherDetails(lat, long).filterNotNull().first()
            }

            if(weatherDetails != null){
                showWeatherNotification(weatherDetails)
                return Result.success()
            } else{
                Log.e("TAG", "doWork: Failed to fetch weather details within timeout", )
                return Result.retry()
            }
        } catch(th: Throwable){
            Log.e("TAG", "doWork: InSide the catch ${th.message}")
            return Result.failure()
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance).apply {
                description = CHANNEL_DESCRIPTION
            }
            // Register the channel with the system
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun showWeatherNotification(weatherDetails: WeatherDetails){
        val notificationManager = (applicationContext.getSystemService(Context.NOTIFICATION_SERVICE)) as NotificationManager
        val temperature = weatherDetails.temp
        val description = weatherDetails.description

        val builder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(mapIconToResourceId(weatherDetails.icon))
            .setContentTitle("Weather Update")
            .setContentText("Current temperature is $temperature K: $description")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        notificationManager.notify(NOTIFICATION_ID, builder.build())
    }
}