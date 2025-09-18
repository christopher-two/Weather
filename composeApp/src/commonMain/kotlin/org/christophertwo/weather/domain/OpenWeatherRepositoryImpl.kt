package org.christophertwo.weather.domain

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import org.christophertwo.weather.domain.dto.OpenWeatherResponse
import org.christophertwo.weather.domain.model.Weather

class OpenWeatherRepositoryImpl(
    private val client: HttpClient
) : WeatherRepository {

    private val apiKey = "b82c388a025d1667eac5d505c7c37b22"
    private val baseUrl = "https://api.openweathermap.org/data/2.5/weather"

    override suspend fun fetchWeatherByCity(city: String): Result<Weather> {
        return try {
            val response: OpenWeatherResponse = client.get(baseUrl) {
                parameter("q", city)
                parameter("appid", apiKey)
                parameter("units", "metric")
                parameter("lang", "es")
            }.body()

            // Log para depuración: verificar que la API devuelve dt y timezone
            println("[OpenWeatherRepository] response.dt=${response.dt} response.timezone=${response.timezone} response.name=${response.name}")

            Result.success(response.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

// Mapping extension
private fun OpenWeatherResponse.toDomain(): Weather {
    val desc = this.weather.firstOrNull()?.description
    return Weather(
        city = this.name ?: "",
        country = this.sys?.country,
        temperature = this.main?.temp,
        description = desc,
        humidity = this.main?.humidity,
        pressure = this.main?.pressure,
        windSpeed = this.wind?.speed,
        timezoneOffsetSeconds = this.timezone,
        timestampUtcSeconds = this.dt
    )
}
