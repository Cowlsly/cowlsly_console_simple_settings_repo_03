package com.cowlsly.simplesettings.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.BatteryFull
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CloudSync
import androidx.compose.material.icons.filled.DeveloperMode
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material.icons.filled.Accessibility
import androidx.compose.ui.graphics.vector.ImageVector
import com.cowlsly.simplesettings.ui.theme.CowlslyCyan
import com.cowlsly.simplesettings.ui.theme.CowlslyGoldLight

fun settingsIconFor(key: String?): ImageVector = when (key) {
    "volume" -> Icons.AutoMirrored.Filled.VolumeUp
    "sound" -> Icons.AutoMirrored.Filled.VolumeUp
    "display" -> Icons.Default.PhoneAndroid
    "spotify" -> Icons.Default.MusicNote
    "security" -> Icons.Default.Lock
    "privacy" -> Icons.Default.PrivacyTip
    "location" -> Icons.Default.LocationOn
    "personal" -> Icons.Default.Person
    "casmea" -> Icons.Default.Favorite
    "network" -> Icons.Default.Wifi
    "bluetooth" -> Icons.Default.Bluetooth
    "device" -> Icons.Default.Storage
    "battery" -> Icons.Default.BatteryFull
    "accessibility" -> Icons.Default.Accessibility
    "apps" -> Icons.Default.Apps
    "notifications" -> Icons.Default.Notifications
    "account", "cowlsly" -> Icons.Default.AccountCircle
    "developer" -> Icons.Default.DeveloperMode
    "devops" -> Icons.Default.CloudSync
    "hidden" -> Icons.Default.VisibilityOff
    "shizuku" -> Icons.Default.Terminal
    "about" -> Icons.Default.Info
    "credits" -> Icons.Default.Build
    else -> Icons.Default.Settings
}

fun settingsIconTintFor(key: String?) = when (key) {
    "casmea", "cowlsly" -> CowlslyGoldLight
    "developer", "devops", "hidden", "shizuku" -> CowlslyCyan
    else -> CowlslyCyan.copy(alpha = 0.85f)
}