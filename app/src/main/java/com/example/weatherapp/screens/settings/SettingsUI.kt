package com.example.weatherapp.screens.settings

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.weatherapp.R
import com.example.weatherapp.ui.theme.OnPrimary
import com.example.weatherapp.ui.theme.Primary

@Preview
@Composable
fun SettingsScreen() {
    val kelvin = stringResource(R.string.kelvin)
    val celsius = stringResource(R.string.celsius)
    val fahrenheit = stringResource(R.string.fahrenheit)
    val mps = stringResource(R.string.mps)
    val mph = stringResource(R.string.mph)
    val gps = stringResource(R.string.gps)
    val map = stringResource(R.string.map)
    val english = stringResource(R.string.english)
    val arabic = stringResource(R.string.arabic)
    val systemDefault = stringResource(R.string.system_default)

    val langOptions = listOf(english, arabic, systemDefault)
    val tempOptions = listOf(kelvin, celsius, fahrenheit)
    val locationOptions = listOf(gps, map)

    val selectedLanguage = remember { mutableStateOf(english) }
    val selectedTempUnit = remember { mutableStateOf(kelvin) }
    val selectedLocation = remember { mutableStateOf(gps) }

    val selectedWindSpeedUnit by remember(selectedTempUnit.value) {
        mutableStateOf(if (selectedTempUnit.value == fahrenheit) mph else mps)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Primary)
            .padding(
                top = 16.dp,
                start = 16.dp,
                end = 16.dp
            )
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = stringResource(R.string.settings_label),
                color = Color.White,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 50.dp)
            ) {
                item {
                    SettingsItem(
                        R.drawable.language,
                        R.string.language,
                        langOptions,
                        selectedOption = selectedLanguage.value,
                        onOptionSelected = { selectedLanguage.value = it }
                    )
                }
                item {
                    SettingsItem(
                        R.drawable.temperature,
                        R.string.temp_unit,
                        tempOptions,
                        selectedOption = selectedTempUnit.value,
                        onOptionSelected = { selectedTempUnit.value = it }
                    )
                }
                item {
                    SettingsItem(
                        R.drawable.location,
                        R.string.location,
                        locationOptions,
                        selectedOption = selectedLocation.value,
                        onOptionSelected = { selectedLocation.value = it }
                    )
                }
                item {
                    SettingsItem(
                        R.drawable.wind,
                        R.string.wind_unit,
                        listOf(mps, mph),
                        selectedOption = selectedWindSpeedUnit,
                        onOptionSelected = {}
                    )
                }
            }
        }
        Button(
            onClick = {
                //viewModel.saveSettingsToDataStore()
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.save_settings))
        }
    }
}

@Composable
fun SettingsItem(
    iconRes: Int,
    titleRes: Int,
    options: List<String>,
    selectedOption: String? = null,
    onOptionSelected: (String) -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .background(OnPrimary),
        shape = RoundedCornerShape(20.dp),
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
            Column(
                modifier = Modifier.selectableGroup()
            ) {
                options.forEach { option ->
                    Row(
                        modifier = Modifier
                            .padding(
                                horizontal = 16.dp,
                                vertical = 16.dp
                            )
                            .selectable(
                                selected = (option == selectedOption),
                                onClick = { onOptionSelected(option) },
                                role = Role.RadioButton
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (option == selectedOption),
                            onClick = null
                        )
                        Text(
                            text = option,
                            color = Color.White,
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    }
                }
            }
        }
    }
}
