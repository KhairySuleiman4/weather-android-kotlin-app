package com.example.weatherapp.screens.settings

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.weatherapp.model.repos.AppRepoImp
import io.mockk.coVerify
import io.mockk.mockk
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SettingsViewModelTest{
    private lateinit var settingsViewModel: SettingsViewModel
    private lateinit var repo: AppRepoImp

    @Before
    fun setUp(){
        repo = mockk(relaxed = true)
        settingsViewModel = SettingsViewModel(repo)
    }

    @Test
    fun saveSettings_DummyValuesForSettings_PropertiesUpdatedCorrectly(){
        // Given -> generated dummy double repo using mockk,  dummy data to be saved
        val newLang = "Arabic"
        val newTemp = "Celsius °C"
        val newLocation = "Map"
        val newWind = "mph"

        // When -> calling the tested function with passing the dummy parameters
        settingsViewModel.saveSettings(newLang, newTemp, newLocation, newWind)

        // Then -> ensuring that properties are assigned correctly
        assertThat(settingsViewModel.language.value, `is`(newLang))
        assertThat(settingsViewModel.temp.value, `is`(newTemp))
        assertThat(settingsViewModel.location.value, `is`(newLocation))
        assertThat(settingsViewModel.wind.value, `is`(newWind))
    }

    @Test
    fun saveSettings_DummyValuesForSettings_AreFunctionsInRepoCalled(){
        // Given -> generated mock double repo using mockk,  dummy data to be saved
        val newLang = "Arabic"
        val newTemp = "Celsius °C"
        val newLocation = "Map"
        val newWind = "mph"

        // When -> calling the tested function with passing the dummy parameters
        settingsViewModel.saveSettings(newLang, newTemp, newLocation, newWind)

        // Then -> ensuring that the repo functions are called even once
        coVerify { repo.writeLanguageChoice(newLang) }
        coVerify { repo.writeTemperatureUnit(newTemp) }
        coVerify { repo.writeLocationChoice(newLocation) }
        coVerify { repo.writeWindSpeedUnit(newWind) }
    }
}