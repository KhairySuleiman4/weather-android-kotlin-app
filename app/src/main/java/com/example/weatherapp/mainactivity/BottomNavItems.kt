package com.example.weatherapp.mainactivity

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings

object BottomNavItems {
    val BottomNavItems = listOf(
        BottomNavItem(
            label = "Home",
            icon = Icons.Filled.Home,
            route = "home"
        ),
        BottomNavItem(
            label = "Favorite",
            icon = Icons.Filled.Favorite,
            route = "favorite"
        ),
        BottomNavItem(
            label = "Alarms",
            icon = Icons.Filled.Notifications,
            route = "alarms"
        ),
        BottomNavItem(
            label = "Settings",
            icon = Icons.Filled.Settings,
            route = "settings"
        )
    )
}