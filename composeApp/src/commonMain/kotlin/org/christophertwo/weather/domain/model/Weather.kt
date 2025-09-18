package org.christophertwo.weather.domain.model

/**
 * Modelo de dominio para el clima. Se añaden campos opcionales para
 * timezone (desplazamiento en segundos respecto a UTC) y dt (timestamp
 * en segundos) retornados por la API de OpenWeather para poder calcular
 * la hora local del lugar consultado.
 */
data class Weather(
    val city: String,
    val country: String?,
    val temperature: Double?,
    val description: String?,
    val humidity: Int?,
    val pressure: Int?,
    val windSpeed: Double?,
    val timezoneOffsetSeconds: Int? = null,
    val timestampUtcSeconds: Long? = null
)
