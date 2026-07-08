package com.cowlsly.simplesettings.data

import android.provider.Settings

/**
 * Full settings catalog — every standard Android category plus gated developer,
 * Shizuku tools, and credits. Base priority P1 = closest to you.
 */
object SettingsCatalog {
    const val PAGE_SIZE = 6

    val allEntries: List<SettingsEntry> = buildList {
        // ── P1: Closest to you ──────────────────────────────────────────────
        add(
            SettingsEntry(
                id = "volume",
                title = "Volume & mute",
                subtitle = "Safe steps: 0 / 25 / 50 / 75 / 90%",
                keywords = listOf("sound", "audio", "mute", "media", "hearing"),
                basePriority = 1,
                panelType = SettingsPanelType.VOLUME,
                iconKey = "volume",
            )
        )
        add(
            SettingsEntry(
                id = "panel_tint",
                title = "Panel colour tint",
                subtitle = "Console glass panel hue",
                keywords = listOf("colour", "color", "theme", "tint", "ui"),
                basePriority = 1,
                panelType = SettingsPanelType.PANEL_TINT,
            )
        )
        add(
            SettingsEntry(
                id = "brightness_quick",
                title = "Brightness feel",
                subtitle = "Quick display brightness shortcut",
                keywords = listOf("display", "screen", "bright"),
                basePriority = 1,
                panelType = SettingsPanelType.SYSTEM_INTENT,
                intentAction = Settings.ACTION_DISPLAY_SETTINGS,
            )
        )

        // ── P2: Sound & display ─────────────────────────────────────────────
        add(intent("sound_settings", "Sound", "Volume, vibration, tones", 2, Settings.ACTION_SOUND_SETTINGS, "sound", "audio", "ringtone"))
        add(
            SettingsEntry(
                id = "spotify_default",
                title = "Spotify as default music",
                subtitle = "Set Spotify as your default music app",
                keywords = listOf("spotify", "music", "default", "media", "audio"),
                basePriority = 2,
                panelType = SettingsPanelType.SPOTIFY_DEFAULT,
            )
        )
        add(intent("display_settings", "Display", "Brightness, dark theme, font size", 2, Settings.ACTION_DISPLAY_SETTINGS, "screen", "theme", "font"))
        add(intent("night_display", "Night Light", "Blue-light filter schedule", 2, Settings.ACTION_NIGHT_DISPLAY_SETTINGS, "night", "blue light"))
        add(intent("zen_mode", "Do Not Disturb", "Notification interruptions", 2, "android.settings.ZEN_MODE_SETTINGS", "dnd", "silent"))

        // ── P3: Security ────────────────────────────────────────────────────
        add(intent("security", "Security", "Screen lock, biometrics, encryption", 3, Settings.ACTION_SECURITY_SETTINGS, "pin", "password", "fingerprint", "lock"))
        add(intent("biometric", "Biometric enroll", "Fingerprint & face setup", 3, Settings.ACTION_BIOMETRIC_ENROLL, "fingerprint", "face"))
        add(intent("trust_agents", "Trust agents", "Smart Lock trusted devices", 3, "android.settings.TRUST_AGENT_SETTINGS", "smart lock"))
        add(
            SettingsEntry(
                id = "lock_on_background",
                title = "Lock when backgrounded",
                subtitle = "Require PIN when app leaves foreground",
                keywords = listOf("lock", "background", "security"),
                basePriority = 3,
                panelType = SettingsPanelType.IN_APP_TOGGLE,
            )
        )

        // ── P4: Privacy ─────────────────────────────────────────────────────
        add(intent("privacy_dashboard", "Privacy dashboard", "Data sharing overview", 4, Settings.ACTION_PRIVACY_SETTINGS, "privacy", "data"))
        add(intent("location", "Location", "GPS & app location access", 4, Settings.ACTION_LOCATION_SOURCE_SETTINGS, "gps", "location"))
        add(intent("manage_all_apps", "All app permissions", "Per-app permission manager", 4, Settings.ACTION_MANAGE_ALL_APPLICATIONS_SETTINGS, "permissions", "apps"))
        add(intent("usage_access", "Usage access", "Apps with usage stats access", 4, Settings.ACTION_USAGE_ACCESS_SETTINGS, "usage"))
        add(intent("overlay", "Display over other apps", "Draw-on-top permission", 4, Settings.ACTION_MANAGE_OVERLAY_PERMISSION, "overlay", "bubble"))

        // ── P5: Personal & CASMEA ───────────────────────────────────────────
        add(
            SettingsEntry(
                id = "casmea_entry",
                title = "CASMEA medical info",
                subtitle = "Emergency card data — edit only here",
                keywords = listOf("medical", "emergency", "casmea", "health", "allergy", "blood"),
                basePriority = 5,
                panelType = SettingsPanelType.CASMEA_ENTRY,
                requiresPin = true,
                iconKey = "casmea",
            )
        )
        add(intent("user_dictionary", "Personal dictionary", "Custom words for keyboard", 5, Settings.ACTION_USER_DICTIONARY_SETTINGS, "dictionary", "keyboard"))

        // ── P6: Network & connectivity ──────────────────────────────────────
        add(intent("wifi", "Wi‑Fi", "Wireless networks", 6, Settings.ACTION_WIFI_SETTINGS, "wireless", "internet"))
        add(intent("wireless", "Network & internet", "Mobile data, Wi‑Fi, VPN", 6, Settings.ACTION_WIRELESS_SETTINGS, "data", "sim"))
        add(intent("data_roaming", "Mobile network", "Cellular & roaming", 6, Settings.ACTION_DATA_ROAMING_SETTINGS, "roaming", "lte", "5g"))
        add(intent("bluetooth", "Bluetooth", "Paired devices", 6, Settings.ACTION_BLUETOOTH_SETTINGS, "headphones", "speaker"))
        add(intent("nfc", "NFC", "Near-field communication", 6, Settings.ACTION_NFC_SETTINGS, "tap", "pay"))
        add(intent("vpn", "VPN", "Virtual private networks", 6, Settings.ACTION_VPN_SETTINGS, "vpn"))
        add(intent("tether", "Hotspot & tethering", "USB / Wi‑Fi hotspot", 6, "android.settings.TETHER_SETTINGS", "hotspot", "usb"))
        add(intent("airplane", "Aeroplane mode", "Disable all radios", 6, Settings.ACTION_AIRPLANE_MODE_SETTINGS, "flight"))

        // ── P7: Device ──────────────────────────────────────────────────────
        add(intent("storage", "Storage", "Free space & app storage", 7, Settings.ACTION_INTERNAL_STORAGE_SETTINGS, "disk", "space"))
        add(intent("battery", "Battery", "Usage & battery saver", 7, Settings.ACTION_BATTERY_SAVER_SETTINGS, "power", "charge"))
        add(intent("battery_usage", "Battery usage", "Per-app consumption", 7, Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS, "drain"))
        add(intent("date_time", "Date & time", "Timezone & automatic time", 7, Settings.ACTION_DATE_SETTINGS, "clock", "timezone"))
        add(intent("locale", "Languages", "System language & region", 7, Settings.ACTION_LOCALE_SETTINGS, "language"))
        add(intent("accessibility", "Accessibility", "TalkBack, captions, display size", 7, Settings.ACTION_ACCESSIBILITY_SETTINGS, "talkback", "a11y"))
        add(intent("captioning", "Captions", "Subtitle preferences", 7, Settings.ACTION_CAPTIONING_SETTINGS, "subtitles"))
        add(intent("hard_keyboard", "Physical keyboard", "Hardware keyboard layout", 7, Settings.ACTION_HARD_KEYBOARD_SETTINGS, "keyboard"))
        add(intent("input_method", "On-screen keyboard", "Keyboard apps & layout", 7, Settings.ACTION_INPUT_METHOD_SETTINGS, "keyboard", "ime"))
        add(intent("device_info", "About phone", "Model, Android version, legal", 7, Settings.ACTION_DEVICE_INFO_SETTINGS, "phone", "build", "version"))

        // ── P8: Apps ────────────────────────────────────────────────────────
        add(intent("applications", "All apps", "Installed applications", 8, Settings.ACTION_APPLICATION_SETTINGS, "apps", "installed"))
        add(intent("manage_apps", "App management", "Force stop, storage, defaults", 8, Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS, "apps"))
        add(intent("default_apps", "Default apps", "Browser, phone, SMS defaults", 8, Settings.ACTION_MANAGE_DEFAULT_APPS_SETTINGS, "defaults"))
        add(intent("unknown_sources", "Install unknown apps", "Sideload permission per app", 8, Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, "sideload", "apk"))
        add(intent("write_settings", "Modify system settings", "Apps that can change settings", 8, Settings.ACTION_MANAGE_WRITE_SETTINGS, "write settings"))
        add(intent("notification_listener", "Notification access", "Apps reading notifications", 8, Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS, "notifications"))
        add(intent("picture_in_picture", "Picture-in-picture", "PiP allowed apps", 8, "android.settings.PICTURE_IN_PICTURE_SETTINGS", "pip"))

        // ── P9: Notifications ─────────────────────────────────────────────
        add(intent("notification_settings", "Notifications", "App notification channels", 9, Settings.ACTION_APP_NOTIFICATION_SETTINGS, "alerts", "channels"))
        add(intent("notification_policy", "DND access", "Apps controlling Do Not Disturb", 9, Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS, "dnd"))
        add(intent("channel_notification", "Notification history", "Recent notification log", 9, "android.settings.NOTIFICATION_HISTORY_SETTINGS", "history"))

        // ── P10: Accounts & backup ──────────────────────────────────────────
        add(
            SettingsEntry(
                id = "cowlsly_account",
                title = "Cowlsly account sync",
                subtitle = "Add Cowlsly to device Accounts — self-syncing with website & apps",
                keywords = listOf("cowlsly", "account", "sync", "login", "website"),
                basePriority = 10,
                panelType = SettingsPanelType.COWLSLY_ACCOUNT,
            )
        )
        add(intent("sync", "Accounts & sync", "Google & other accounts", 10, Settings.ACTION_SYNC_SETTINGS, "google", "account"))
        add(intent("backup", "Backup", "Google backup & restore", 10, "android.settings.BACKUP_AND_RESET_SETTINGS", "restore"))
        add(intent("add_account", "Add account", "Sign in to services", 10, "android.settings.ADD_ACCOUNT_SETTINGS", "sign in"))

        // ── P11: Developer (gated) ──────────────────────────────────────────
        add(
            SettingsEntry(
                id = "developer_gate",
                title = "Developer tools",
                subtitle = "PIN required — opens dev options & suite tools",
                keywords = listOf("developer", "debug", "adb", "usb"),
                basePriority = 11,
                panelType = SettingsPanelType.DEVELOPER_GATE,
                requiresDeveloperAccess = true,
                requiresPin = true,
                iconKey = "developer",
            )
        )
        add(devIntent("dev_options", "Developer options", "Android system developer settings", Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS, "debug", "developer"))
        add(devIntent("wireless_debug", "Wireless debugging", "ADB over Wi‑Fi", "android.settings.WIRELESS_DEBUGGING_SETTINGS", "adb", "wifi debug"))
        add(devIntent("developer_graphics", "Graphics driver", "GPU driver preferences", "android.settings.DEVELOPMENT_SETTINGS", "gpu", "graphics"))
        add(devIntent("memory", "Running services", "Active processes & RAM", Settings.ACTION_MEMORY_CARD_SETTINGS, "ram", "process"))
        add(devIntent("dream", "Screensaver", "Daydream / screensaver settings", Settings.ACTION_DREAM_SETTINGS, "screensaver"))

        // ── Shizuku & privileged tools ──────────────────────────────────────
        add(
            SettingsEntry(
                id = "shizuku_status",
                title = "Shizuku",
                subtitle = "Privileged API for hidden settings",
                keywords = listOf("shizuku", "root", "shell", "privileged", "hidden"),
                basePriority = 11,
                panelType = SettingsPanelType.SHIZUKU_TOOL,
                requiresDeveloperAccess = true,
            )
        )
        add(devIntent("appops", "App ops", "Hidden per-permission toggles", Settings.ACTION_USAGE_ACCESS_SETTINGS, "appops", "hidden"))
        add(devIntent("locale_secret", "Regional preferences", "Advanced locale overrides", Settings.ACTION_LOCALE_SETTINGS, "locale", "region"))

        // ── P12: About & sync ───────────────────────────────────────────────
        add(
            SettingsEntry(
                id = "about",
                title = "About Simple Settings",
                subtitle = "Version, grant developer access, export sync JSON",
                keywords = listOf("version", "about", "cowlsly", "sync"),
                basePriority = 12,
                panelType = SettingsPanelType.ABOUT,
            )
        )
        add(
            SettingsEntry(
                id = "credits",
                title = "Credits",
                subtitle = "Fair credit to external app makers we learn from",
                keywords = listOf("credits", "acknowledgements", "thanks", "shizuku", "hidden settings"),
                basePriority = 13,
                panelType = SettingsPanelType.CREDITS,
            )
        )
    }

    private fun intent(
        id: String,
        title: String,
        subtitle: String,
        priority: Int,
        action: String,
        vararg keywords: String,
    ): SettingsEntry = SettingsEntry(
        id = id,
        title = title,
        subtitle = subtitle,
        keywords = keywords.toList(),
        basePriority = priority,
        panelType = SettingsPanelType.SYSTEM_INTENT,
        intentAction = action,
    )

    private fun devIntent(
        id: String,
        title: String,
        subtitle: String,
        action: String,
        vararg keywords: String,
    ): SettingsEntry = SettingsEntry(
        id = id,
        title = title,
        subtitle = subtitle,
        keywords = keywords.toList(),
        basePriority = 11,
        panelType = SettingsPanelType.SYSTEM_INTENT,
        intentAction = action,
        requiresDeveloperAccess = true,
    )
}