package com.example.weatherapp.screens.home

import android.Manifest
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.example.weatherapp.R
import kotlinx.coroutines.delay

@Composable
fun HomeScreen(viewModel: HomeViewModel) {
    val context = LocalContext.current
    val deny = stringResource(R.string.permission_denied)

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
    ) { permissions ->
        val bothGranted = permissions.all { it.value }
        if(bothGranted)
            viewModel.getCurrentLocation()
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
            } else
                viewModel.getCurrentLocation()
        } else
            viewModel.getLocationFromDataStore()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Home,
            contentDescription = "home",
            tint = Color(0xFF0F9D58)
        )
        Text(text = "Home", color = Color.Black)
    }
}