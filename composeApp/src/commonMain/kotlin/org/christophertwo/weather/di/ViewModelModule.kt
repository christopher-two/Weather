package org.christophertwo.weather.di

import org.christophertwo.weather.presentation.features.main.MainViewModel
import org.christophertwo.weather.domain.usecase.FetchWeatherUseCase
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val ViewModelModule: Module
    get() = module {
        single { FetchWeatherUseCase(get()) }
        viewModelOf(::MainViewModel)
    }