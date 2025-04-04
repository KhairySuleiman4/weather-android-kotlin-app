package com.example.weatherapp.screens.alarms

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.weatherapp.model.notificationsworker.NotificationsWorker
import com.example.weatherapp.model.pojos.local.Notification
import com.example.weatherapp.model.pojos.local.weather.WeatherDetails
import com.example.weatherapp.model.repos.AppRepoImp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class AlarmsViewModel(private val repo: AppRepoImp) : ViewModel(){

    private var workManager: WorkManager? = null

    private val mutableSchedulingStatus = MutableStateFlow<SchedulingStatus>(SchedulingStatus.Idle)
    val schedulingStatus = mutableSchedulingStatus.asStateFlow()
    /*private val mutableNotificationScheduled = MutableStateFlow<Long?>(null)
    val notificationScheduled = mutableNotificationScheduled.asStateFlow()*/
    private val mutableNotificationsList = MutableStateFlow<List<Notification>>(emptyList())
    val notificationsList = mutableNotificationsList.asStateFlow()

    init {
        getStoredNotifications()
    }

    fun initialize(context: Context){
        if(workManager == null){
            workManager = WorkManager.getInstance(context)
        }
    }

    private fun getStoredNotifications(){
        viewModelScope.launch(Dispatchers.IO) {
            repo.getAllNotifications().collect{
                mutableNotificationsList.value = it
            }
        }
    }

    fun scheduleWeatherNotification(toBeScheduledTime: Long){
        val currentTimeMillis = System.currentTimeMillis()
        if (toBeScheduledTime <= currentTimeMillis) {
            mutableSchedulingStatus.value = SchedulingStatus.Error("Cannot schedule notification in the past")
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            repo.insertNotification(
                Notification(time = toBeScheduledTime)
            )
        }

        viewModelScope.launch {
            mutableSchedulingStatus.value = SchedulingStatus.Loading
            try {
                workManager?.let { manager ->
                    val delayInMillis = toBeScheduledTime - currentTimeMillis

                    val weatherWorkRequest = OneTimeWorkRequestBuilder<NotificationsWorker>()
                        .setInitialDelay(delayInMillis, TimeUnit.MILLISECONDS)
                        .build()
                    manager.enqueueUniqueWork(
                        "weather_notification_${toBeScheduledTime}",
                        ExistingWorkPolicy.REPLACE,
                        weatherWorkRequest
                    )
                    mutableSchedulingStatus.value = SchedulingStatus.Success
                } ?: run {
                    mutableSchedulingStatus.value = SchedulingStatus.Error("WorkManager not initialized")
                }
            } catch (th: Throwable){
                mutableSchedulingStatus.value = SchedulingStatus.Error(th.message ?: "Unknown error occurred")
            }
        }
    }

    fun deleteNotification(time: Long){
        cancelNotification(time)
        viewModelScope.launch(Dispatchers.IO) {
            repo.deleteNotification(time)
        }
    }

    private fun cancelNotification(timeInMillis: Long) {
        viewModelScope.launch {
            workManager?.cancelUniqueWork("weather_notification_${timeInMillis}")
        }
    }
}

class AlarmsFactory(private val repo: AppRepoImp): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AlarmsViewModel(repo) as T
    }
}

sealed class SchedulingStatus {
    object Idle : SchedulingStatus()
    object Loading : SchedulingStatus()
    object Success : SchedulingStatus()
    data class Error(val message: String) : SchedulingStatus()
}