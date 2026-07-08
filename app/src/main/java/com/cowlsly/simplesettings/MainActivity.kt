package com.cowlsly.simplesettings

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.cowlsly.simplesettings.ui.SimpleSettingsRoot
import com.cowlsly.simplesettings.ui.theme.SimpleSettingsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val repository = (application as SimpleSettingsApplication).settingsRepository
        setContent {
            SimpleSettingsTheme {
                SimpleSettingsRoot(repository = repository)
            }
        }
    }
}