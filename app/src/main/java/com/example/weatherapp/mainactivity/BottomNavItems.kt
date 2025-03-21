package com.example.weatherapp.mainactivity

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import com.example.weatherapp.R

object BottomNavItems {
    val BottomNavItems = listOf(
        BottomNavItem(
            label = R.string.home_label,
            icon = Icons.Filled.Home,
            route = "home"
        ),
        BottomNavItem(
            label = R.string.favorite_label,
            icon = Icons.Filled.Favorite,
            route = "favorite"
        ),
        BottomNavItem(
            label = R.string.alarms_label,
            icon = Icons.Filled.Notifications,
            route = "alarms"
        ),
        BottomNavItem(
            label = R.string.settings_label,
            icon = Icons.Filled.Settings,
            route = "settings"
        )
    )
}