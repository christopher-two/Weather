package org.christophertwo.weather

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import kotlinx.browser.document
import org.christophertwo.weather.di.DomainModule
import org.christophertwo.weather.di.ViewModelModule
import org.koin.core.context.startKoin

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    ComposeViewport(
        viewportContainer = document.body!!
    ) {
        App()
    }
}