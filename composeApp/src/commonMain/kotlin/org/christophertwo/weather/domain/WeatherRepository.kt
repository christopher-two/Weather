package org.christophertwo.weather.domain

import org.christophertwo.weather.domain.model.Weather

interface WeatherRepository {
    suspend fun fetchWeatherByCity(city: String): Result<Weather>
}

