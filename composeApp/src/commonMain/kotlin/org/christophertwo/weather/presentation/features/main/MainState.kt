package org.christophertwo.weather.presentation.features.main

data class MainState(
    val time: TimeState = TimeState(),
    val date: DateState = DateState(),
    val seconds: SecondsState = SecondsState(),
    val city: CityState = CityState(),
    val sun: SunState = SunState(),
    val weather: WeatherState = WeatherState(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val backgroundColor: Long? = null
) {
    data class TimeState(
        val hours: String = "08",
        val minutes: String = "40",
        val isLoading: Boolean = false,
        val isEmpty: Boolean = false,
        val error: String? = null,
    )

    data class DateState(
        val day: String = "Thu,",
        val date: String = "20 Mar",
        val isLoading: Boolean = false,
        val isEmpty: Boolean = false,
        val error: String? = null,
    )

    data class SecondsState(
        val prev: String = "14",
        val current: String = "15",
        val next: String = "16",
        val isLoading: Boolean = false,
        val isEmpty: Boolean = false,
        val error: String? = null,
    )

    data class CityState(
        val text: String = "Los Angeles, California, USA",
        val isLoading: Boolean = false,
        val isEmpty: Boolean = false,
        val error: String? = null,
    )

    data class SunState(
        val info: String = "Sun · 10h 06m",
        val range: String = "07:12 - 17:17",
        val isLoading: Boolean = false,
        val isEmpty: Boolean = false,
        val error: String? = null,
    )

    data class WeatherState(
        val temperature: String = "--°C",
        val description: String? = null,
        val humidity: String? = null,
        val pressure: String? = null,
        val windSpeed: String? = null,
        val isLoading: Boolean = false,
        val isEmpty: Boolean = true,
        val error: String? = null,
    )
}
