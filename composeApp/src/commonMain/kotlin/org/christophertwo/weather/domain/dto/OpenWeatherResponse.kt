package org.christophertwo.weather.domain.dto

import kotlinx.serialization.Serializable

@Serializable
data class OpenWeatherResponse(
    val name: String? = null,
    val dt: Long? = null,
    val timezone: Int? = null,
    val sys: SysInfo? = null,
    val main: MainInfo? = null,
    val weather: List<WeatherInfo> = emptyList(),
    val wind: WindInfo? = null
)

@Serializable
data class SysInfo(val country: String? = null)

@Serializable
data class MainInfo(
    val temp: Double? = null,
    val humidity: Int? = null,
    val pressure: Int? = null
)

@Serializable
data class WeatherInfo(val description: String? = null)

@Serializable
data class WindInfo(val speed: Double? = null)

