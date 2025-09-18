package org.christophertwo.weather.di

import io.ktor.client.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.christophertwo.weather.domain.OpenWeatherRepositoryImpl
import org.christophertwo.weather.domain.WeatherRepository
import org.christophertwo.weather.domain.usecase.FetchWeatherUseCase
import org.koin.core.module.Module
import org.koin.dsl.module

val DomainModule: Module
    get() = module {
        single<WeatherRepository> { OpenWeatherRepositoryImpl(get()) }
        single { FetchWeatherUseCase(get()) }
        single<HttpClient> {
            HttpClient {
                expectSuccess = true
                install(io.ktor.client.plugins.contentnegotiation.ContentNegotiation) {
                    json(Json {
                        prettyPrint = true
                        isLenient = true
                        ignoreUnknownKeys = true
                    })
                }
            }
        }
    }

