package org.christophertwo.weather.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import org.christophertwo.weather.core.RoutesApp
import org.christophertwo.weather.presentation.features.main.MainRoot

@Composable
fun NavigationApp(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = RoutesApp.Main.route
    ) {
        composable(RoutesApp.Main.route) {
            MainRoot()
        }
    }
}