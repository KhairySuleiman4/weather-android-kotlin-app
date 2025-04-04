package com.example.weatherapp.screens.favorite

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherapp.R
import com.example.weatherapp.model.pojos.local.weather.WeatherDetails
import com.example.weatherapp.screens.favoritedetails.FavoriteDetails
import com.example.weatherapp.screens.mapactivity.MapActivity
import com.example.weatherapp.ui.theme.Background
import com.example.weatherapp.ui.theme.Primary

@Composable
fun FavoriteScreen(viewModel: FavoriteViewModel) {
    viewModel.isConnectedToInternet()
    val context = LocalContext.current
    val favCities = viewModel.favCities.collectAsState()
    val isConnected = viewModel.isConnected.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Primary)
    ) {
        Column {
            Text(
                text = stringResource(R.string.favorite_cities),
                color = Color.White,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(16.dp)
            )
            LazyColumn(
                modifier = Modifier.fillMaxWidth()
            ) {
                items(favCities.value.size) {
                    FavCity(
                        weatherDetails = favCities.value[it],
                        onDeleteClick = { city ->
                            viewModel.deleteCity(city.cityId)
                        },
                        onCityClick = { city ->
                            val toFavDetails = Intent(context, FavoriteDetails::class.java)
                            toFavDetails.putExtra("lat", city.lat)
                            toFavDetails.putExtra("long", city.lon)
                            toFavDetails.putExtra("id", city.cityId)
                            context.startActivity(toFavDetails)
                        }
                    )
                }
            }
        }

        FloatingActionButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            onClick = {
                if(isConnected.value){
                    context.startActivity(
                        Intent(context, MapActivity::class.java).putExtra("caller", "favorite")
                    )
                } else{
                    Toast.makeText(context, R.string.connect_to_internet, Toast.LENGTH_SHORT).show()
                }
            }
        ) {
            Icon(Icons.Filled.Add, contentDescription = stringResource(R.string.add_city))
        }
    }
}

@Composable
fun FavCity(weatherDetails: WeatherDetails, onDeleteClick: (WeatherDetails) -> Unit, onCityClick: (WeatherDetails) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .height(60.dp)
            .clickable { onCityClick(weatherDetails) },
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
                text = weatherDetails.cityName,
                fontSize = 24.sp,
                color = Color.White
            )
            IconButton(
                onClick = { onDeleteClick(weatherDetails) }
            ) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "Delete city",
                    tint = Color.Red
                )
            }
        }
    }
}
