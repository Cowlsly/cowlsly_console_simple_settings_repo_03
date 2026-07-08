package com.cowlsly.simplesettings.data

import androidx.compose.ui.graphics.vector.ImageVector

enum class SettingsPanelType {
    VOLUME,
    PANEL_TINT,
    CASMEA_ENTRY,
    DEVELOPER_GATE,
    SYSTEM_INTENT,
    IN_APP_TOGGLE,
    SHIZUKU_TOOL,
    CREDITS,
    ABOUT,
    COWLSLY_ACCOUNT,
    SPOTIFY_DEFAULT,
}

data class SettingsEntry(
    val id: String,
    val title: String,
    val subtitle: String = "",
    val keywords: List<String> = emptyList(),
    val basePriority: Int,
    val panelType: SettingsPanelType,
    val intentAction: String? = null,
    val intentExtraKey: String? = null,
    val intentExtraValue: String? = null,
    val requiresDeveloperAccess: Boolean = false,
    val requiresPin: Boolean = false,
    val iconKey: String? = null,
) {
    fun matchesQuery(query: String): Boolean {
        if (query.isBlank()) return true
        val q = query.trim().lowercase()
        return title.lowercase().contains(q) ||
            subtitle.lowercase().contains(q) ||
            keywords.any { it.lowercase().contains(q) }
    }
}