package org.christophertwo.weather.core

sealed class RoutesApp(val route: String) {
    object Main : RoutesApp("main")
    object Settings : RoutesApp("settings")
}