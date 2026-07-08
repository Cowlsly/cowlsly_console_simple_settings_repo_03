package com.cowlsly.simplesettings.ui.theme

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp

val LocalPanelTint = compositionLocalOf { 0.5f }

fun tintedGlassColor(base: Color, tint: Float): Color {
    val cyan = CowlslyCyan.copy(alpha = 0.15f + tint * 0.25f)
    return lerp(base, cyan, tint.coerceIn(0f, 1f) * 0.45f)
}

fun tintedBorderColor(tint: Float): Color {
    return lerp(GlassBorder, CowlslyGoldLight.copy(alpha = 0.7f), tint.coerceIn(0f, 1f) * 0.35f)
}