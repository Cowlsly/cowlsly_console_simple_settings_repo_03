package com.cowlsly.simplesettings.sync

import android.provider.Settings

data class SyncBinding(
    val entryId: String,
    val source: SyncSource,
    val key: String? = null,
    val control: SyncControl = SyncControl.READ_ONLY,
    val valueFormatter: ((String) -> String)? = null,
)

object SettingsSyncRegistry {
    val bindings: Map<String, SyncBinding> = listOf(
        SyncBinding("volume", SyncSource.AUDIO, control = SyncControl.FULL),
        SyncBinding("panel_tint", SyncSource.LOCAL, control = SyncControl.LOCAL_EXPORT),
        SyncBinding("brightness_quick", SyncSource.SYSTEM, Settings.System.SCREEN_BRIGHTNESS, SyncControl.READ_ONLY),
        SyncBinding("display_settings", SyncSource.SYSTEM, Settings.System.SCREEN_BRIGHTNESS, SyncControl.READ_ONLY),
        SyncBinding("airplane", SyncSource.GLOBAL, Settings.Global.AIRPLANE_MODE_ON, SyncControl.READ_ONLY),
        SyncBinding("location", SyncSource.SECURE, Settings.Secure.LOCATION_MODE, SyncControl.READ_ONLY),
        SyncBinding("adb_enabled", SyncSource.GLOBAL, Settings.Global.ADB_ENABLED, SyncControl.READ_ONLY, ::formatOnOff),
        SyncBinding("spotify_default", SyncSource.INTENT, control = SyncControl.READ_ONLY),
        SyncBinding("cowlsly_account", SyncSource.LOCAL, control = SyncControl.LOCAL_EXPORT),
        SyncBinding("devops_tools", SyncSource.INTENT, control = SyncControl.READ_ONLY),
        SyncBinding("hidden_device_apps", SyncSource.INTENT, control = SyncControl.READ_ONLY),
        SyncBinding("appops", SyncSource.INTENT, control = SyncControl.READ_ONLY),
        SyncBinding("clipboard", SyncSource.INTENT, control = SyncControl.READ_ONLY),
        SyncBinding("data_saver", SyncSource.INTENT, control = SyncControl.READ_ONLY),
        SyncBinding("cast", SyncSource.INTENT, control = SyncControl.READ_ONLY),
        SyncBinding("dev_options", SyncSource.GLOBAL, Settings.Global.DEVELOPMENT_SETTINGS_ENABLED, SyncControl.READ_ONLY, ::formatOnOff),
        SyncBinding("lock_on_background", SyncSource.LOCAL, control = SyncControl.FULL),
        SyncBinding("casmea_entry", SyncSource.LOCAL, control = SyncControl.LOCAL_EXPORT),
        SyncBinding("wifi", SyncSource.INTENT, control = SyncControl.READ_ONLY),
        SyncBinding("bluetooth", SyncSource.INTENT, control = SyncControl.READ_ONLY),
        SyncBinding("battery", SyncSource.INTENT, control = SyncControl.READ_ONLY),
        SyncBinding("security", SyncSource.INTENT, control = SyncControl.READ_ONLY),
        SyncBinding("privacy_dashboard", SyncSource.INTENT, control = SyncControl.READ_ONLY),
        SyncBinding("zen_mode", SyncSource.GLOBAL, "zen_mode", SyncControl.READ_ONLY, ::formatZen),
    ).associateBy { it.entryId }

    fun bindingFor(entryId: String): SyncBinding = bindings[entryId]
        ?: SyncBinding(
            entryId = entryId,
            source = SyncSource.INTENT,
            control = SyncControl.READ_ONLY,
        )

    private fun formatOnOff(raw: String): String = when (raw) {
        "1", "true" -> "On"
        "0", "false" -> "Off"
        else -> raw
    }

    private fun formatZen(raw: String): String = when (raw) {
        "0" -> "Off"
        "1" -> "Important interruptions only"
        "2" -> "Alarms only"
        "3" -> "Total silence"
        else -> raw
    }
}