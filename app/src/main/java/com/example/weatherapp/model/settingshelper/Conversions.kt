package com.example.weatherapp.model.settingshelper

import android.annotation.SuppressLint

fun Double.toCelsius(): Int{
    return (this - 273.15).toInt()
}

fun Double.toFahrenheit(): Int{
    return ((this - 273.15) * 1.8 + 32).toInt()
}

fun Double.toMilePerHour(): Double{
    return this * 2.237
}

@SuppressLint("DefaultLocale")
fun Double.trimToTwoDecimals(): String {
    return String.format("%.2f", this)
}
