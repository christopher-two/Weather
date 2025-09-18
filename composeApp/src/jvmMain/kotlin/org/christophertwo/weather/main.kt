package org.christophertwo.weather

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.christophertwo.weather.di.ViewModelModule
import org.koin.core.context.startKoin

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Weather",
    ) {
        App()
    }
}