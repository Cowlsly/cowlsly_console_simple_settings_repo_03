package com.cowlsly.simplesettings.ui.panels

import android.provider.Settings
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.cowlsly.simplesettings.data.SettingsEntry
import com.cowlsly.simplesettings.data.SettingsPanelType
import com.cowlsly.simplesettings.data.SystemIntentLauncher
import com.cowlsly.simplesettings.ui.components.SettingsPanelHeader

private data class HiddenShortcut(
    val label: String,
    val subtitle: String,
    val entry: SettingsEntry,
)

@Composable
fun HiddenDeviceAppsPanel(
    entry: SettingsEntry,
    onOpened: () -> Unit = {},
) {
    val context = LocalContext.current
    val shortcuts = hiddenShortcuts()

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        SettingsPanelHeader(entry = entry)
        Text(
            "How to open: tap any button — we try the system intent, then a known AOSP component, " +
                "then a safe parent settings screen. Same pattern as Hidden Settings / Activity Launcher.",
            style = MaterialTheme.typography.bodySmall,
        )
        shortcuts.forEach { shortcut ->
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                OutlinedButton(
                    onClick = {
                        onOpened()
                        SystemIntentLauncher.launch(context, shortcut.entry)
                    },
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(shortcut.label)
                }
                Text(shortcut.subtitle, style = MaterialTheme.typography.labelSmall)
            }
        }
    }
}

private fun hidden(
    id: String,
    label: String,
    subtitle: String,
    action: String,
    componentPackage: String? = null,
    componentClass: String? = null,
    fallbacks: List<String> = emptyList(),
): HiddenShortcut = HiddenShortcut(
    label = label,
    subtitle = subtitle,
    entry = SettingsEntry(
        id = id,
        title = label,
        subtitle = subtitle,
        basePriority = 11,
        panelType = SettingsPanelType.SYSTEM_INTENT,
        intentAction = action,
        requiresDeveloperAccess = true,
        componentPackage = componentPackage,
        componentClass = componentClass,
        fallbackActions = fallbacks,
    ),
)

private fun hiddenShortcuts(): List<HiddenShortcut> = listOf(
    hidden(
        id = "hidden_appops",
        label = "App ops (hidden toggles)",
        subtitle = "Tap → Per-permission switches not shown in normal Settings",
        action = "android.settings.APP_OPS_SETTINGS",
        componentPackage = "com.android.settings",
        componentClass = "com.android.settings.Settings\$AppOpsSummaryActivity",
        fallbacks = listOf(Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS),
    ),
    hidden(
        id = "hidden_usage",
        label = "Usage access",
        subtitle = "Tap → Apps reading your usage statistics",
        action = Settings.ACTION_USAGE_ACCESS_SETTINGS,
    ),
    hidden(
        id = "hidden_notif_listener",
        label = "Notification listener access",
        subtitle = "Tap → Apps that read notification content",
        action = Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS,
    ),
    hidden(
        id = "hidden_overlay",
        label = "Display over other apps",
        subtitle = "Tap → Draw-on-top / overlay permission",
        action = Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
    ),
    hidden(
        id = "hidden_write_settings",
        label = "Modify system settings",
        subtitle = "Tap → Apps allowed to write secure settings",
        action = Settings.ACTION_MANAGE_WRITE_SETTINGS,
    ),
    hidden(
        id = "hidden_device_admin",
        label = "Device admin apps",
        subtitle = "Tap → Enterprise controllers & device policies",
        action = "android.settings.DEVICE_ADMIN_SETTINGS",
        componentPackage = "com.android.settings",
        componentClass = "com.android.settings.Settings\$DeviceAdminSettingsActivity",
    ),
    hidden(
        id = "hidden_running",
        label = "Running services",
        subtitle = "Tap → Active processes and RAM usage",
        action = "android.settings.RUNNING_SERVICES",
        componentPackage = "com.android.settings",
        componentClass = "com.android.settings.Settings\$RunningServicesActivity",
        fallbacks = listOf(Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS),
    ),
    hidden(
        id = "hidden_all_apps",
        label = "All installed apps",
        subtitle = "Tap → Full application list with system apps",
        action = Settings.ACTION_MANAGE_ALL_APPLICATIONS_SETTINGS,
    ),
    hidden(
        id = "hidden_dev_dashboard",
        label = "Development settings dashboard",
        subtitle = "Tap → Full Developer options activity",
        action = Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS,
        componentPackage = "com.android.settings",
        componentClass = "com.android.settings.Settings\$DevelopmentSettingsDashboardActivity",
    ),
    hidden(
        id = "hidden_wireless_debug",
        label = "Wireless debugging",
        subtitle = "Tap → ADB over Wi‑Fi",
        action = "android.settings.WIRELESS_DEBUGGING_SETTINGS",
        componentPackage = "com.android.settings",
        componentClass = "com.android.settings.Settings\$WirelessDebuggingActivity",
        fallbacks = listOf(Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS),
    ),
    hidden(
        id = "hidden_special_access",
        label = "Special app access",
        subtitle = "Tap → Alarms, unrestricted data, full-screen",
        action = "android.settings.SPECIAL_ACCESS_SETTINGS",
        fallbacks = listOf(Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS),
    ),
    hidden(
        id = "hidden_battery_opt",
        label = "Battery optimization",
        subtitle = "Tap → Ignore battery optimizations list",
        action = Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS,
    ),
    hidden(
        id = "hidden_vpn",
        label = "VPN",
        subtitle = "Tap → Virtual private networks",
        action = Settings.ACTION_VPN_SETTINGS,
    ),
    hidden(
        id = "hidden_print",
        label = "Printing",
        subtitle = "Tap → Print services",
        action = Settings.ACTION_PRINT_SETTINGS,
    ),
    hidden(
        id = "hidden_webview",
        label = "WebView implementation",
        subtitle = "Tap → System WebView provider",
        action = Settings.ACTION_WEBVIEW_SETTINGS,
    ),
    hidden(
        id = "hidden_dream",
        label = "Screensaver / Daydream",
        subtitle = "Tap → Ambient display apps",
        action = Settings.ACTION_DREAM_SETTINGS,
    ),
    HiddenShortcut(
        label = "App ops summary (component only)",
        subtitle = "Tap → Direct component when intent action is blocked",
        entry = SettingsEntry(
            id = "hidden_appops_component",
            title = "App ops summary",
            basePriority = 11,
            panelType = SettingsPanelType.SYSTEM_INTENT,
            requiresDeveloperAccess = true,
            componentPackage = "com.android.settings",
            componentClass = "com.android.settings.Settings\$AppOpsSummaryActivity",
            intentAction = "android.settings.APP_OPS_SETTINGS",
        ),
    ),
    HiddenShortcut(
        label = "Development settings (component only)",
        subtitle = "Tap → Graphics & advanced dev prefs activity",
        entry = SettingsEntry(
            id = "hidden_dev_component",
            title = "Development settings",
            basePriority = 11,
            panelType = SettingsPanelType.SYSTEM_INTENT,
            requiresDeveloperAccess = true,
            intentAction = Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS,
            componentPackage = "com.android.settings",
            componentClass = "com.android.settings.Settings\$DevelopmentSettingsDashboardActivity",
        ),
    ),
)
