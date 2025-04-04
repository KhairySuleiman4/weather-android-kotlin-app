package com.example.weatherapp.screens.alarms

import android.content.Intent
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.weatherapp.R
import com.example.weatherapp.screens.mapactivity.MapActivity
import com.example.weatherapp.ui.theme.Primary
import com.google.android.material.datepicker.MaterialDatePicker
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.weatherapp.model.pojos.local.Notification
import com.example.weatherapp.ui.theme.Background
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlarmsScreen(viewModel: AlarmsViewModel) {
    val context = LocalContext.current
    viewModel.initialize(context)
    val notificationsList = viewModel.notificationsList.collectAsState()

    val calendar = Calendar.getInstance()
    var selectedDate by remember { mutableLongStateOf(calendar.timeInMillis) }

    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    /*val schedulingStatus by viewModel.schedulingStatus.collectAsState()
    val notificationScheduled by viewModel.notificationScheduled.collectAsState()
    val selectDate = stringResource(R.string.select_date)*/

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Primary)
    ) {
        Column {
            Text(
                text = stringResource(R.string.notifications_screen),
                color = Color.White,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(16.dp)
            )
            LazyColumn(
                modifier = Modifier.fillMaxWidth()
            ) {
                items(notificationsList.value.size){
                    NotificationItem(
                        notification = notificationsList.value[it],
                        onDeleteClick = {
                            viewModel.deleteNotification(it.time)
                        }
                    )
                }
            }
        }
        FloatingActionButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            onClick = { showDatePicker = true }
        ) {
            Icon(Icons.Filled.Add, contentDescription = stringResource(R.string.add_a_notification))
        }

        if(showDatePicker){
            val datePickerState = rememberDatePickerState(
                initialSelectedDateMillis = selectedDate
            )
            DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                confirmButton = {
                    Button(
                        onClick = {
                            datePickerState.selectedDateMillis?.let { dateInMillis ->
                                val newCalendar = Calendar.getInstance()
                                newCalendar.timeInMillis = dateInMillis

                                val oldCalendar = Calendar.getInstance()
                                oldCalendar.timeInMillis = selectedDate

                                newCalendar.set(
                                    Calendar.HOUR_OF_DAY,
                                    oldCalendar.get(Calendar.HOUR_OF_DAY)
                                )
                                newCalendar.set(Calendar.MINUTE, oldCalendar.get(Calendar.MINUTE))

                                selectedDate = newCalendar.timeInMillis
                            }
                            showDatePicker = false
                            showTimePicker = true
                        }
                    ) {
                        Text(stringResource(R.string.confirm))
                    }
                },
                dismissButton = {
                    Button(onClick = { showDatePicker = false }) {
                        Text(stringResource(R.string.cancel))
                    }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }

        if(showTimePicker){
            TimePickerDialog(
                onDismiss = { showTimePicker = false },
                onConfirm = { hour, minute ->
                    val newCalendar = Calendar.getInstance()
                    newCalendar.timeInMillis = selectedDate
                    newCalendar.set(Calendar.HOUR_OF_DAY, hour)
                    newCalendar.set(Calendar.MINUTE, minute)
                    selectedDate = newCalendar.timeInMillis

                    viewModel.scheduleWeatherNotification(selectedDate)

                    showTimePicker = false
                }
            )
        }
    }
}

@Composable
fun NotificationItem(notification: Notification, onDeleteClick: (Notification) -> Unit) {
    val formattedTime = remember(notification.time) {
        val calendar = Calendar.getInstance().apply {
            timeInMillis = notification.time
        }
        val dateFormat = SimpleDateFormat("MMM dd, yyyy - HH:mm", Locale.getDefault())
        dateFormat.format(calendar.time)
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .height(60.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(Background)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = formattedTime,
                fontSize = 24.sp,
                color = Color.White
            )
            IconButton(
                onClick = { onDeleteClick(notification) }
            ) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "Delete notification",
                    tint = Color.Red
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerDialog(
    onDismiss: () -> Unit,
    onConfirm: (hour: Int, minute: Int) -> Unit
) {
    val timePickerState = rememberTimePickerState()

    Dialog(onDismissRequest = { onDismiss() }) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.select_time),
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(16.dp))

                TimePicker(state = timePickerState)

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = { onDismiss() }) {
                        Text(stringResource(R.string.cancel))
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(onClick = {
                        onConfirm(timePickerState.hour, timePickerState.minute)
                    }) {
                        Text(stringResource(R.string.confirm))
                    }
                }
            }
        }
    }
}