package com.cowlsly.simplesettings.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val CowlslyColorScheme = darkColorScheme(
    primary = CowlslyCyan,
    onPrimary = CowlslyBgDeep,
    secondary = CowlslyGoldMid,
    onSecondary = CowlslyBgDeep,
    tertiary = CowlslyGoldLight,
    background = CowlslyBgDeep,
    onBackground = Color.White,
    surface = GlassPanel,
    onSurface = Color.White,
    error = CowlslyWarning,
)

@Composable
fun SimpleSettingsTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = CowlslyColorScheme,
        content = content,
    )
}