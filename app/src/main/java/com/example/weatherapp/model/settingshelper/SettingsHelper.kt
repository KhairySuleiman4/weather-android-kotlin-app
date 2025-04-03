package com.example.weatherapp.model.settingshelper

import android.app.LocaleManager
import android.content.Context
import android.os.Build
import android.os.LocaleList
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.weatherapp.R
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Locale

private val Context.dataStore by preferencesDataStore("settings_prefs")

class SettingsHelper(private val context: Context){

    companion object{
        private val LANGUAGE_KEY = stringPreferencesKey("language_key")
        private val TEMPERATURE_UNIT_KEY = stringPreferencesKey("temperature_unit_key")
        private val LOCATION_KEY = stringPreferencesKey("location_key")
        private val WIND_SPEED_UNIT_KEY = stringPreferencesKey("wind_speed_unit_key")
        private val LAT_LONG_KEY = stringPreferencesKey("lat_long_key")
    }

    val language: Flow<String> = context.dataStore.data.map {
        it[LANGUAGE_KEY] ?: "English"
    }

    val temperatureUnit: Flow<String> = context.dataStore.data.map {
        it[TEMPERATURE_UNIT_KEY] ?: "Kelvin"
    }

    val location: Flow<String> = context.dataStore.data.map {
        it[LOCATION_KEY] ?: "GPS"
    }

    val windSpeedUnit: Flow<String> = context.dataStore.data.map {
        it[WIND_SPEED_UNIT_KEY] ?: "m/s"
    }

    val latLong: Flow<Pair<String, String>> = context.dataStore.data.map { it ->
        it[LAT_LONG_KEY]?.split(",").let{
          if(it?.size == 2)
              it[0] to it[1]
          else
              "0" to "0"
        }
    }

    suspend fun writeLanguage(lang: String){
        context.dataStore.edit {
            it[LANGUAGE_KEY] = lang
        }
        when(lang){
            context.getString(R.string.english) -> changeLanguage("en", context)
            context.getString(R.string.arabic) -> changeLanguage("ar", context)
        }
    }

    suspend fun writeTemperatureUnit(tempUnit: String){
        context.dataStore.edit {
            it[TEMPERATURE_UNIT_KEY] = tempUnit
        }
    }

    suspend fun writeLocation(location: String){
        context.dataStore.edit {
            it[LOCATION_KEY] = location
        }
    }

    suspend fun writeWindSpeedUnit(wind: String){
        context.dataStore.edit {
            it[WIND_SPEED_UNIT_KEY] = wind
        }
    }

    suspend fun writeLatLong(lat: Double, long: Double){
        context.dataStore.edit {
            it[LAT_LONG_KEY] = "$lat,$long"
        }
    }

    private fun changeLanguage(languageCode: String, context: Context) {
        //version >= 13
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.getSystemService(LocaleManager::class.java).applicationLocales =
                LocaleList.forLanguageTags(languageCode)
        } else {
            //version < 13
            setAppLocale(languageCode, context)
        }
    }

    private fun setAppLocale(languageCode: String, context: Context) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        val config = context.resources.configuration
        config.setLocale(locale)
        config.setLayoutDirection(locale)

        context.createConfigurationContext(config)
        context.resources.updateConfiguration(config, context.resources.displayMetrics)
    }
}