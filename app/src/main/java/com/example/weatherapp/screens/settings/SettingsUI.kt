package com.example.weatherapp.screens.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.weatherapp.R
import com.example.weatherapp.ui.theme.OnPrimary
import com.example.weatherapp.ui.theme.Primary

@Preview
@Composable
fun SettingsScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Primary)
            .padding(16.dp)
    ) {
        Text(
            text = stringResource(R.string.settings_label),
            color = Color.White,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                SettingsItem(
                    R.drawable.language,
                    R.string.language,
                    listOf(
                        stringResource(R.string.arabic),
                        stringResource(R.string.english),
                        stringResource(R.string.system_default)
                    )
                )
            }
            item {
                SettingsItem(
                    R.drawable.temperature,
                    R.string.temp_unit,
                    listOf(
                        stringResource(R.string.celsius),
                        stringResource(R.string.kelvin),
                        stringResource(R.string.fahrenheit)
                    )
                )
            }
            item {
                SettingsItem(
                    R.drawable.location,
                    R.string.location,
                    listOf(
                        stringResource(R.string.gps),
                        stringResource(R.string.map)
                    )
                )
            }
            item {
                SettingsItem(
                    R.drawable.wind,
                    R.string.wind_unit,
                    listOf(
                        stringResource(R.string.mps),
                        stringResource(R.string.mph)
                    )
                )
            }
        }
    }
}

@Composable
fun SettingsItem(iconRes: Int, titleRes: Int, options: List<String>) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .background(OnPrimary),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = OnPrimary),
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(iconRes),
                    contentDescription = stringResource(titleRes),
                    modifier = Modifier
                        .size(36.dp)
                        .padding(end = 12.dp)
                )
                Text(
                    text = stringResource(titleRes),
                    color = Color.White,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            Spacer(
                modifier = Modifier.height(8.dp)
            )
            options.forEach { option ->
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = false,
                        onClick = {
                        // viewModel.applyLogic
                    })
                    Text(
                        text = option,
                        color = Color.White,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        }
    }
}
