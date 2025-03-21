package com.example.weatherapp.mainactivity

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavItem(
    @StringRes val label: Int,
    val icon: ImageVector,
    val route: String
)