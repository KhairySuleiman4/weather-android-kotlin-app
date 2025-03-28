package com.example.weatherapp.model.settingshelper

import android.annotation.SuppressLint

fun Double.toCelsius(): String{
    return (this - 273.15).toInt().toString()
}

fun Double.toFahrenheit(): String{
    return ((this - 273.15) * 1.8 + 32).toInt().toString()
}

fun Double.toMilePerHour(): String{
    return (this * 2.237).trimToTwoDecimals()
}

@SuppressLint("DefaultLocale")
fun Double.trimToTwoDecimals(): String {
    return String.format("%.2f", this)
}
