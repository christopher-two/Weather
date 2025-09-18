package org.christophertwo.weather.domain.usecase

import org.christophertwo.weather.domain.WeatherRepository
import org.christophertwo.weather.domain.model.Weather

class FetchWeatherUseCase(private val repository: WeatherRepository) {
    suspend operator fun invoke(city: String): Result<Weather> {
        return repository.fetchWeatherByCity(city)
    }
}

