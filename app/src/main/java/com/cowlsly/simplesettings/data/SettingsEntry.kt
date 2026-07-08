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
    HIDDEN_DEVICE_APPS,
    DEVOPS_TOOLS,
}

data class SettingsEntry(
    val id: String,
    val title: String,
    val subtitle: String = "",
    val keywords: List<String> = emptyList(),
    val basePriority: Int,
    val panelType: SettingsPanelType,
    /** Primary [android.content.Intent] action (e.g. [android.provider.Settings.ACTION_WIFI_SETTINGS]). */
    val intentAction: String? = null,
    val intentExtraKey: String? = null,
    val intentExtraValue: String? = null,
    /** When true, intent data is `package:<this app>` (needed for some permission screens). */
    val intentPackageData: Boolean = false,
    /** Optional absolute data URI (overrides [intentPackageData] when set). */
    val intentDataUri: String? = null,
    /** Explicit AOSP / OEM component if the action is missing on a device. */
    val componentPackage: String? = null,
    val componentClass: String? = null,
    /** Secondary intent actions tried when the primary action cannot resolve. */
    val fallbackActions: List<String> = emptyList(),
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