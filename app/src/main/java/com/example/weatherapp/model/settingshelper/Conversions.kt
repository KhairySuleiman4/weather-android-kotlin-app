package com.example.weatherapp.model.settingshelper

import android.annotation.SuppressLint

fun Double.toCelsius(): Double{
    return (this - 273.15)
}

fun Double.toFahrenheit(): Double{
    return ((this - 273.15) * 1.8 + 32)
}

fun Double.toMilePerHour(): Double{
    return 0.0//(this.trimToTwoDecimals() * 2.24)
}

fun Double.trimToTwoDecimals(): Double{
    return String.format("%.2f", this).toDouble()
}
