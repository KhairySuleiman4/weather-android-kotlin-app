package com.example.weatherapp.model.settingshelper

import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Locale

fun Double.toCelsius(): Double{
    return (this - 273.15)
}

fun Double.toFahrenheit(): Double{
    return ((this - 273.15) * 1.8 + 32)
}

fun Double.toMilePerHour(): Double{
    return (this * 2.24).toString().substring(0, 4).toDouble()
}

fun formatNumber(value: Int): String {
    return NumberFormat.getInstance(Locale.getDefault()).format(value)
}

fun formatNumber(value: Double): String {
    return NumberFormat.getInstance(Locale.getDefault()).format(value)
}

fun formatDateBasedOnLocale(dateString: String): String {
    val locale = Locale.getDefault()
    val inputFormat = SimpleDateFormat("HH:mm", Locale.ENGLISH)
    val outputFormat = SimpleDateFormat("HH:mm", locale)
    return try {
        val date = inputFormat.parse(dateString)
        date?.let { outputFormat.format(it) } ?: "Invalid date"
    } catch (e: Exception) {
        "Invalid date format"
    }
}

fun translateWeatherDescription(desc: String): String{
    return if (Locale.getDefault() == Locale.ENGLISH){
        desc
    } else{
        when(desc){
            "thunderstorm with light rain", "thunderstorm with light drizzle" -> "عاصفة رعدية مع أمطار خفيفة"
            "thunderstorm with rain", "thunderstorm with drizzle" -> "عاصفة رعدية مع أمطار"
            "thunderstorm with heavy rain", "thunderstorm with heavy drizzle" -> "عاصفة رعدية مع أمطار غزيرة"
            "light thunderstorm" -> "عاصفة رعدية خفيفة"
            "thunderstorm" -> "عاصفة رعدية"
            "heavy thunderstorm" -> "عاصفة رعدية شديدة"
            "ragged thunderstorm" -> "عاصفة رعدية متقطّعة"
            "light intensity drizzle", "drizzle", "heavy intensity drizzle", "light intensity drizzle rain", "light rain" -> "أمطار خفيفة"
            "drizzle rain", "heavy intensity drizzle rain", "moderate rain" -> "أمطار"
            "shower rain and drizzle", "heavy shower rain and drizzle", "shower drizzle", "shower rain" -> "أمطار متقطّعة"
            "heavy intensity rain" -> "أمطار غزيرة"
            "very heavy rain", "extreme rain" -> "أمطار غزيرة جدا (سيول)"
            "freezing rain", "rain and snow" -> "أمطار جليدية"
            "light intensity shower rain" -> "أمطار خفيفة متقطّعة"
            "heavy intensity shower rain", "ragged shower rain" -> "أمطار غزيرة متقطّعة"
            "light snow", "snow", "heavy snow", "sleet", "light shower sleet", "shower sleet", "light rain and snow", "light shower snow", "shower snow", "heavy shower snow" -> "جليد"
            "mist", "smoke", "haze", "sand/dust whirls", "fog", "sand", "dust", "volcanic ash", "squalls", "tornado"  -> "ضباب"
            "clear sky" -> "سماء صافية"
            "few clouds" -> "سُحُب خفيفة"
            "scattered clouds" -> "سُحُب متفرقة"
            "broken clouds" -> "سُحُب متقطّعة"
            "overcast clouds" -> "سُحُب كثيفة"
            else -> "وصف الطقس"
        }
    }
}