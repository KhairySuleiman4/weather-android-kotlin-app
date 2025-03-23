package com.example.weatherapp.model.repos

import com.example.weatherapp.model.repos.settings.SettingsRepoImp

class AppRepoImp(private val settingsRepo: SettingsRepoImp): AppRepo {
    companion object{
        @Volatile
        private var instance: AppRepoImp? = null

        fun getInstance(settingsRepo: SettingsRepoImp): AppRepoImp {
            if (instance == null) {
                synchronized(this) {
                    if (instance == null) {
                        instance = AppRepoImp(settingsRepo)
                    }
                }
            }
            return instance!!
        }
    }

    override fun readLanguageChoice() = settingsRepo.readLanguageChoice()

    override fun readTemperatureUnit() = settingsRepo.readTemperatureUnit()

    override fun readLocationChoice() = settingsRepo.readLocationChoice()

    override fun readWindSpeedUnit() = settingsRepo.readWindSpeedUnit()


    override suspend fun writeLanguageChoice(lang: String) = settingsRepo.writeLanguageChoice(lang)

    override suspend fun writeTemperatureUnit(temp: String) = settingsRepo.writeTemperatureUnit(temp)

    override suspend fun writeLocationChoice(location: String) = settingsRepo.writeLocationChoice(location)

    override suspend fun writeWindSpeedUnit(wind: String) = settingsRepo.writeWindSpeedUnit(wind)
}