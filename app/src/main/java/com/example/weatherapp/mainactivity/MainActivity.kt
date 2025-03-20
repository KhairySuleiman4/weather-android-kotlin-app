package com.example.weatherapp.mainactivity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.OnBackPressedCallback
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.example.weatherapp.screens.alarms.AlarmsScreen
import com.example.weatherapp.screens.favorite.FavoriteScreen
import com.example.weatherapp.screens.home.HomeScreen
import com.example.weatherapp.screens.settings.SettingsScreen
import com.example.weatherapp.ui.theme.Primary

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()
            HandleBackPress(navController = navController)
            Scaffold(
                bottomBar = {
                    BottomNavigationBar(navController = navController)
                },
                content = { padding ->
                    NavHostContainer(navController = navController, padding = padding)
                }
            )
        }
    }
}

@Composable
fun NavHostContainer(
    navController: NavHostController,
    padding: PaddingValues
) {
    NavHost(
        navController = navController,
        startDestination = "home",
        modifier = Modifier.padding(paddingValues = padding)
    ) {
        composable("home") {
            HomeScreen()
        }
        composable("favorite") {
            FavoriteScreen()
        }
        composable("alarms") {
            AlarmsScreen()
        }
        composable("settings") {
            SettingsScreen()
        }
    }
}

@Composable
fun HandleBackPress(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: "home"

    val activity = (navController.context as? ComponentActivity)

    DisposableEffect(navController) {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (currentRoute == "home") {
                    activity?.finish()
                } else {
                    navController.navigate("home") {
                        popUpTo("home") { inclusive = true }
                    }
                }
            }
        }
        activity?.onBackPressedDispatcher?.addCallback(callback)

        onDispose {
            callback.remove()
        }
    }
}


@Composable
fun BottomNavigationBar(navController: NavHostController) {
    NavigationBar(
        containerColor = Primary
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        BottomNavItems.BottomNavItems.forEach { navItem ->
            NavigationBarItem(
                selected = currentRoute == navItem.route,
                onClick = {
                    navController.navigate(navItem.route) {
                        popUpTo("home") { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(imageVector = navItem.icon, contentDescription = navItem.label)
                },
                label = {
                    Text(text = navItem.label)
                },
                alwaysShowLabel = false,
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.White,
                    unselectedIconColor = Color.White,
                    selectedTextColor = Color.White,
                    indicatorColor = Primary
                )
            )
        }
    }
}
