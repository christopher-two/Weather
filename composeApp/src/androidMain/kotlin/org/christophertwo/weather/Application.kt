package org.christophertwo.weather

import android.app.Application
import org.koin.core.component.KoinComponent

class Application : Application(), KoinComponent {
    override fun onCreate() {
        super.onCreate()
    }
}