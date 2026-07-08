package com.cowlsly.simplesettings.data

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings

/**
 * Opens Android system settings screens for every catalog row that uses
 * [SettingsPanelType.SYSTEM_INTENT], including gated developer options.
 *
 * Strategy (first success wins):
 * 1. Primary action (+ extras / package data URI when set)
 * 2. Explicit component on the entry
 * 3. Entry-level fallback actions
 * 4. Known AOSP component fallbacks for that action
 * 5. Zone parent (developer → Development Settings; else → Settings root)
 */
object SystemIntentLauncher {

    private val knownComponentFallbacks: Map<String, List<ComponentName>> = mapOf(
        Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS to listOf(
            ComponentName(
                "com.android.settings",
                "com.android.settings.Settings\$DevelopmentSettingsDashboardActivity",
            ),
            ComponentName(
                "com.android.settings",
                "com.android.settings.DevelopmentSettings",
            ),
        ),
        "android.settings.WIRELESS_DEBUGGING_SETTINGS" to listOf(
            ComponentName(
                "com.android.settings",
                "com.android.settings.Settings\$WirelessDebuggingActivity",
            ),
        ),
        "android.settings.APP_OPS_SETTINGS" to listOf(
            ComponentName(
                "com.android.settings",
                "com.android.settings.Settings\$AppOpsSummaryActivity",
            ),
        ),
        "android.settings.RUNNING_SERVICES" to listOf(
            ComponentName(
                "com.android.settings",
                "com.android.settings.Settings\$RunningServicesActivity",
            ),
            ComponentName(
                "com.android.settings",
                "com.android.settings.RunningServices",
            ),
        ),
        "android.settings.DEVELOPMENT_SETTINGS" to listOf(
            ComponentName(
                "com.android.settings",
                "com.android.settings.Settings\$DevelopmentSettingsDashboardActivity",
            ),
        ),
        "android.settings.DEVICE_ADMIN_SETTINGS" to listOf(
            ComponentName(
                "com.android.settings",
                "com.android.settings.Settings\$DeviceAdminSettingsActivity",
            ),
        ),
        Settings.ACTION_MANAGE_ALL_APPLICATIONS_SETTINGS to listOf(
            ComponentName(
                "com.android.settings",
                "com.android.settings.Settings\$ManageApplicationsActivity",
            ),
        ),
        Settings.ACTION_APPLICATION_SETTINGS to listOf(
            ComponentName(
                "com.android.settings",
                "com.android.settings.Settings\$ManageApplicationsActivity",
            ),
        ),
        Settings.ACTION_WIFI_SETTINGS to listOf(
            ComponentName(
                "com.android.settings",
                "com.android.settings.Settings\$WifiSettingsActivity",
            ),
        ),
        Settings.ACTION_BLUETOOTH_SETTINGS to listOf(
            ComponentName(
                "com.android.settings",
                "com.android.settings.Settings\$BluetoothSettingsActivity",
            ),
        ),
        Settings.ACTION_LOCATION_SOURCE_SETTINGS to listOf(
            ComponentName(
                "com.android.settings",
                "com.android.settings.Settings\$LocationSettingsActivity",
            ),
        ),
        Settings.ACTION_SECURITY_SETTINGS to listOf(
            ComponentName(
                "com.android.settings",
                "com.android.settings.Settings\$SecurityDashboardActivity",
            ),
        ),
        Settings.ACTION_PRIVACY_SETTINGS to listOf(
            ComponentName(
                "com.android.settings",
                "com.android.settings.Settings\$PrivacyDashboardActivity",
            ),
        ),
        Settings.ACTION_ACCESSIBILITY_SETTINGS to listOf(
            ComponentName(
                "com.android.settings",
                "com.android.settings.Settings\$AccessibilitySettingsActivity",
            ),
        ),
        Settings.ACTION_DISPLAY_SETTINGS to listOf(
            ComponentName(
                "com.android.settings",
                "com.android.settings.Settings\$DisplaySettingsActivity",
            ),
        ),
        Settings.ACTION_SOUND_SETTINGS to listOf(
            ComponentName(
                "com.android.settings",
                "com.android.settings.Settings\$SoundSettingsActivity",
            ),
        ),
        Settings.ACTION_BATTERY_SAVER_SETTINGS to listOf(
            ComponentName(
                "com.android.settings",
                "com.android.settings.Settings\$BatterySaverSettingsActivity",
            ),
            ComponentName(
                "com.android.settings",
                "com.android.settings.Settings\$PowerUsageSummaryActivity",
            ),
        ),
        Settings.ACTION_INTERNAL_STORAGE_SETTINGS to listOf(
            ComponentName(
                "com.android.settings",
                "com.android.settings.Settings\$StorageDashboardActivity",
            ),
        ),
        Settings.ACTION_DATE_SETTINGS to listOf(
            ComponentName(
                "com.android.settings",
                "com.android.settings.Settings\$DateTimeSettingsActivity",
            ),
        ),
        Settings.ACTION_LOCALE_SETTINGS to listOf(
            ComponentName(
                "com.android.settings",
                "com.android.settings.Settings\$LanguageSettingsActivity",
            ),
            ComponentName(
                "com.android.settings",
                "com.android.settings.Settings\$LocalePickerActivity",
            ),
        ),
        Settings.ACTION_DEVICE_INFO_SETTINGS to listOf(
            ComponentName(
                "com.android.settings",
                "com.android.settings.Settings\$MyDeviceInfoActivity",
            ),
            ComponentName(
                "com.android.settings",
                "com.android.settings.Settings\$DeviceInfoSettingsActivity",
            ),
        ),
        Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS to listOf(
            ComponentName(
                "com.android.settings",
                "com.android.settings.Settings\$NotificationAccessSettingsActivity",
            ),
        ),
        Settings.ACTION_USAGE_ACCESS_SETTINGS to listOf(
            ComponentName(
                "com.android.settings",
                "com.android.settings.Settings\$UsageAccessSettingsActivity",
            ),
        ),
        Settings.ACTION_MANAGE_OVERLAY_PERMISSION to listOf(
            ComponentName(
                "com.android.settings",
                "com.android.settings.Settings\$OverlaySettingsActivity",
            ),
        ),
        Settings.ACTION_MANAGE_WRITE_SETTINGS to listOf(
            ComponentName(
                "com.android.settings",
                "com.android.settings.Settings\$WriteSettingsActivity",
            ),
        ),
        Settings.ACTION_DREAM_SETTINGS to listOf(
            ComponentName(
                "com.android.settings",
                "com.android.settings.Settings\$DreamSettingsActivity",
            ),
        ),
        "android.settings.ZEN_MODE_SETTINGS" to listOf(
            ComponentName(
                "com.android.settings",
                "com.android.settings.Settings\$ZenModeSettingsActivity",
            ),
        ),
        "android.settings.TETHER_SETTINGS" to listOf(
            ComponentName(
                "com.android.settings",
                "com.android.settings.Settings\$TetherSettingsActivity",
            ),
        ),
        "android.settings.USER_SETTINGS" to listOf(
            ComponentName(
                "com.android.settings",
                "com.android.settings.Settings\$UserSettingsActivity",
            ),
        ),
        "android.settings.BACKUP_AND_RESET_SETTINGS" to listOf(
            ComponentName(
                "com.android.settings",
                "com.android.settings.Settings\$PrivacySettingsActivity",
            ),
        ),
    )

    /**
     * @return true if a settings screen was started.
     */
    fun launch(context: Context, entry: SettingsEntry): Boolean {
        val tried = linkedSetOf<String>()

        entry.intentAction?.let { action ->
            if (tryStart(context, buildPrimaryIntent(context, entry, action))) {
                return true
            }
            tried += action
        }

        if (entry.componentPackage != null && entry.componentClass != null) {
            if (tryStart(
                    context,
                    Intent().setComponent(
                        ComponentName(entry.componentPackage, entry.componentClass),
                    ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK),
                )
            ) {
                return true
            }
        }

        for (fallback in entry.fallbackActions) {
            if (fallback in tried) continue
            if (tryStart(context, Intent(fallback).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))) {
                return true
            }
            tried += fallback
            knownComponentFallbacks[fallback]?.forEach { component ->
                if (tryStart(
                        context,
                        Intent().setComponent(component).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK),
                    )
                ) {
                    return true
                }
            }
        }

        entry.intentAction?.let { action ->
            knownComponentFallbacks[action]?.forEach { component ->
                if (tryStart(
                        context,
                        Intent().setComponent(component).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK),
                    )
                ) {
                    return true
                }
            }
        }

        val parent = if (entry.requiresDeveloperAccess) {
            Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS
        } else {
            Settings.ACTION_SETTINGS
        }
        if (parent !in tried && tryStart(context, Intent(parent).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))) {
            return true
        }
        if (entry.requiresDeveloperAccess) {
            knownComponentFallbacks[Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS]
                ?.forEach { component ->
                    if (tryStart(
                            context,
                            Intent().setComponent(component)
                                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK),
                        )
                    ) {
                        return true
                    }
                }
        }
        return tryStart(
            context,
            Intent(Settings.ACTION_SETTINGS).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK),
        )
    }

    /** Open Developer Options directly (gate button / DevOps shortcuts). */
    fun launchDeveloperOptions(context: Context): Boolean {
        return launch(
            context,
            SettingsEntry(
                id = "dev_options_direct",
                title = "Developer options",
                basePriority = 11,
                panelType = SettingsPanelType.SYSTEM_INTENT,
                intentAction = Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS,
                requiresDeveloperAccess = true,
                componentPackage = "com.android.settings",
                componentClass = "com.android.settings.Settings\$DevelopmentSettingsDashboardActivity",
                fallbackActions = listOf("android.settings.APPLICATION_DEVELOPMENT_SETTINGS"),
            ),
        )
    }

    private fun buildPrimaryIntent(
        context: Context,
        entry: SettingsEntry,
        action: String,
    ): Intent {
        return Intent(action).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            entry.intentExtraKey?.let { key ->
                // Prefer explicit value; else this app's package (notification / app screens).
                val value = entry.intentExtraValue
                    ?: if (key == Settings.EXTRA_APP_PACKAGE || entry.intentPackageData) {
                        context.packageName
                    } else {
                        ""
                    }
                putExtra(key, value)
            }
            if (entry.intentPackageData) {
                data = Uri.parse("package:${context.packageName}")
            }
            entry.intentDataUri?.let { data = Uri.parse(it) }
        }
    }

    private fun tryStart(context: Context, intent: Intent): Boolean {
        return runCatching {
            // Explicit components often omit CATEGORY_DEFAULT — skip resolve gate for them.
            if (intent.component == null) {
                val resolved = context.packageManager.resolveActivity(
                    intent,
                    PackageManager.MATCH_DEFAULT_ONLY,
                )
                if (resolved == null) return@runCatching false
            }
            context.startActivity(intent)
            true
        }.getOrDefault(false)
    }
}
