package org.christophertwo.weather.presentation.features.main

sealed interface MainAction {
    data class FetchWeather(val city: String) : MainAction
    object Refresh : MainAction
}