package com.cowlsly.simplesettings

import android.app.Application
import com.cowlsly.simplesettings.data.SettingsRepository

class SimpleSettingsApplication : Application() {
    lateinit var settingsRepository: SettingsRepository
        private set

    override fun onCreate() {
        super.onCreate()
        settingsRepository = SettingsRepository(this)
    }
}