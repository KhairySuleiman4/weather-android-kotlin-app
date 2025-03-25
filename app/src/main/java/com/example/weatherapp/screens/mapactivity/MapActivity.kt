package com.example.weatherapp.screens.mapactivity

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weatherapp.R
import com.example.weatherapp.model.repos.AppRepoImp
import com.example.weatherapp.model.repos.settings.SettingsRepoImp
import com.example.weatherapp.model.settingshelper.SettingsHelper
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng

import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState

class MapActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MapUI(
                viewModel(
                    factory = MapFactory(
                        AppRepoImp.getInstance(
                            SettingsRepoImp.getInstance(
                                SettingsHelper(this)
                            )
                        )
                    )
                )
            )
        }
    }
}

@Composable
fun MapUI(viewModel: MapViewModel) {
    var markerPosition by remember { mutableStateOf<LatLng?>(null) }
    val cameraPositionState = rememberCameraPositionState() // change the default to be the current location
    val showSaveButton = remember { mutableStateOf(false) }
    val markerState = rememberMarkerState()
    val context = LocalContext.current
    val lat = remember { mutableDoubleStateOf(0.0) }
    val long = remember { mutableDoubleStateOf(0.0) }

    Box(
        modifier = Modifier.fillMaxSize()
    ){
        GoogleMap(
            modifier = Modifier.matchParentSize(),
            cameraPositionState = cameraPositionState,
            onMapClick = { latLng ->
                markerPosition = latLng
                markerState.position = latLng
                cameraPositionState.position = CameraPosition.fromLatLngZoom(latLng, 12f)
                showSaveButton.value = true
                lat.doubleValue = latLng.latitude
                long.doubleValue = latLng.longitude
            }
        ) {
            markerPosition?.let {
                Marker(
                    state = markerState

                )
            }
        }

        if(showSaveButton.value)
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(horizontal = 50.dp, vertical = 16.dp),
                onClick = {
                    viewModel.saveCoordinatesToDataStore(lat.doubleValue, long.doubleValue)
                    (context as? Activity)?.finish()
                }
            ){
                Text(
                    text = stringResource(R.string.save_location)
                )
            }
    }

}
