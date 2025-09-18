package org.christophertwo.weather

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import org.christophertwo.weather.presentation.navigation.NavigationApp
import org.christophertwo.weather.presentation.theme.WeatherTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

import org.koin.core.context.startKoin
import org.christophertwo.weather.di.ViewModelModule
import org.christophertwo.weather.di.DomainModule

@Composable
@Preview
fun App() {
    // Inicia Koin con los módulos de dominio y viewmodels
    startKoin {
        modules(listOf(ViewModelModule, DomainModule))
    }

    WeatherTheme {
        val navController = rememberNavController()
        NavigationApp(
            navController = navController,
        )
    }
}