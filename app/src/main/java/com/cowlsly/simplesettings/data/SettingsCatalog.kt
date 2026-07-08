@file:Suppress("InlinedApi")

package com.cowlsly.simplesettings.data

import android.provider.Settings

/**
 * Full settings catalog — every standard Android category plus gated developer
 * options, hidden device apps, DevOps, Shizuku tools, Spotify, Cowlsly account,
 * and credits.
 *
 * Every [SettingsPanelType.SYSTEM_INTENT] row is clickable via
 * [SystemIntentLauncher] (action → component → fallbacks → parent screen).
 * Developer rows require access; non-developer entries are always available.
 *
 * High-API [Settings] action constants are intentional: [SystemIntentLauncher]
 * falls back when a device does not resolve them (minSdk 26).
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
                iconKey = "display",
            )
        )
        add(
            intent(
                id = "brightness_quick",
                title = "Brightness feel",
                subtitle = "Tap → Display brightness & theme",
                priority = 1,
                action = Settings.ACTION_DISPLAY_SETTINGS,
                "display", "screen", "bright",
                icon = "display",
            )
        )

        // ── P2: Sound & display ─────────────────────────────────────────────
        add(intent("sound_settings", "Sound", "Tap → Volume, vibration, tones", 2, Settings.ACTION_SOUND_SETTINGS, "sound", "audio", "ringtone", "vibration", icon = "sound"))
        add(
            SettingsEntry(
                id = "spotify_default",
                title = "Spotify as default music",
                subtitle = "Set Spotify as your default music app",
                keywords = listOf("spotify", "music", "default", "media", "audio"),
                basePriority = 2,
                panelType = SettingsPanelType.SPOTIFY_DEFAULT,
                iconKey = "spotify",
            )
        )
        add(intent("display_settings", "Display", "Tap → Brightness, dark theme, font size", 2, Settings.ACTION_DISPLAY_SETTINGS, "screen", "theme", "font", "timeout", icon = "display"))
        add(intent("wallpaper", "Wallpaper & style", "Tap → Home screen wallpaper and colours", 2, "android.settings.WALLPAPER_SETTINGS", "wallpaper", "theme", "style", icon = "display", fallbacks = listOf(Settings.ACTION_DISPLAY_SETTINGS)))
        add(intent("night_display", "Night Light", "Tap → Blue-light filter schedule", 2, Settings.ACTION_NIGHT_DISPLAY_SETTINGS, "night", "blue light", icon = "display", fallbacks = listOf(Settings.ACTION_DISPLAY_SETTINGS)))
        add(intent("auto_rotate", "Auto-rotate", "Tap → Screen rotation lock", 2, Settings.ACTION_AUTO_ROTATE_SETTINGS, "rotate", "orientation", icon = "display", fallbacks = listOf(Settings.ACTION_DISPLAY_SETTINGS)))
        add(intent("zen_mode", "Do Not Disturb", "Tap → Notification interruptions", 2, "android.settings.ZEN_MODE_SETTINGS", "dnd", "silent", icon = "notifications", fallbacks = listOf(Settings.ACTION_ZEN_MODE_PRIORITY_SETTINGS, Settings.ACTION_SOUND_SETTINGS)))
        add(intent("zen_priority", "DND priority", "Tap → Who can interrupt you", 2, Settings.ACTION_ZEN_MODE_PRIORITY_SETTINGS, "dnd", "priority", icon = "notifications", fallbacks = listOf("android.settings.ZEN_MODE_SETTINGS")))
        add(intent("home_settings", "Home app", "Tap → Default launcher / home screen", 2, Settings.ACTION_HOME_SETTINGS, "launcher", "home", icon = "display"))
        add(intent("voice_input", "Voice input", "Tap → Speech recognition & assistant", 2, Settings.ACTION_VOICE_INPUT_SETTINGS, "voice", "speech", "assistant", icon = "sound"))
        add(intent("dream", "Screensaver", "Tap → Daydream / ambient display", 2, Settings.ACTION_DREAM_SETTINGS, "screensaver", "daydream", icon = "display"))
        add(intent("cast", "Cast & nearby", "Tap → Chromecast and nearby share", 2, Settings.ACTION_CAST_SETTINGS, "cast", "chromecast", icon = "network"))

        // ── P3: Security ────────────────────────────────────────────────────
        add(intent("security", "Security", "Tap → Screen lock, biometrics, encryption", 3, Settings.ACTION_SECURITY_SETTINGS, "pin", "password", "fingerprint", "lock", icon = "security"))
        add(
            intent(
                "biometric",
                "Biometric enroll",
                "Tap → Fingerprint & face setup",
                3,
                Settings.ACTION_BIOMETRIC_ENROLL,
                "fingerprint", "face",
                icon = "security",
                fallbacks = listOf(
                    "android.settings.FINGERPRINT_ENROLL",
                    Settings.ACTION_SECURITY_SETTINGS,
                ),
            ),
        )
        add(
            intent(
                "fingerprint",
                "Fingerprint enroll",
                "Tap → Fingerprint only",
                3,
                "android.settings.FINGERPRINT_ENROLL",
                "fingerprint", "touch",
                icon = "security",
                fallbacks = listOf(Settings.ACTION_BIOMETRIC_ENROLL, Settings.ACTION_SECURITY_SETTINGS),
            ),
        )
        add(intent("trust_agents", "Trust agents", "Tap → Smart Lock trusted devices", 3, "android.settings.TRUST_AGENT_SETTINGS", "smart lock", icon = "security", fallbacks = listOf(Settings.ACTION_SECURITY_SETTINGS)))
        add(intent("screen_lock", "Screen lock settings", "Tap → Lock screen timeout & options", 3, "android.settings.SCREEN_LOCK_SETTINGS", "lock screen", "timeout", icon = "security", fallbacks = listOf(Settings.ACTION_SECURITY_SETTINGS)))
        add(intent("credential_provider", "Credential provider", "Tap → Passkeys & password managers", 3, Settings.ACTION_CREDENTIAL_PROVIDER, "passkey", "password", "autofill", icon = "security", fallbacks = listOf(Settings.ACTION_SECURITY_SETTINGS)))
        add(intent("autofill", "Autofill service", "Tap → Choose autofill provider", 3, Settings.ACTION_REQUEST_SET_AUTOFILL_SERVICE, "autofill", "passwords", icon = "security", fallbacks = listOf(Settings.ACTION_SECURITY_SETTINGS)))
        add(
            SettingsEntry(
                id = "lock_on_background",
                title = "Lock when backgrounded",
                subtitle = "Require PIN when app leaves foreground",
                keywords = listOf("lock", "background", "security"),
                basePriority = 3,
                panelType = SettingsPanelType.IN_APP_TOGGLE,
                iconKey = "security",
            )
        )

        // ── P4: Privacy ─────────────────────────────────────────────────────
        add(intent("privacy_dashboard", "Privacy dashboard", "Tap → Data sharing overview", 4, Settings.ACTION_PRIVACY_SETTINGS, "privacy", "data", icon = "privacy"))
        add(intent("location", "Location", "Tap → GPS & app location access", 4, Settings.ACTION_LOCATION_SOURCE_SETTINGS, "gps", "location", icon = "location"))
        add(intent("manage_all_apps", "All app permissions", "Tap → Per-app permission manager", 4, Settings.ACTION_MANAGE_ALL_APPLICATIONS_SETTINGS, "permissions", "apps", icon = "apps"))
        add(intent("usage_access", "Usage access", "Tap → Apps with usage stats access", 4, Settings.ACTION_USAGE_ACCESS_SETTINGS, "usage", icon = "privacy"))
        add(
            intent(
                id = "overlay",
                title = "Display over other apps",
                subtitle = "Tap → Draw-on-top permission",
                priority = 4,
                action = Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                "overlay", "bubble",
                icon = "privacy",
                packageData = true,
            )
        )
        add(intent("clipboard", "Clipboard access", "Tap → Apps that read your clipboard", 4, "android.settings.CLIPBOARD_ACCESS_SETTINGS", "clipboard", "paste", icon = "privacy", fallbacks = listOf(Settings.ACTION_PRIVACY_SETTINGS)))
        add(intent("special_access", "Special app access", "Tap → Unrestricted data, alarms, full-screen", 4, "android.settings.SPECIAL_ACCESS_SETTINGS", "special", "access", "alarms", icon = "privacy", fallbacks = listOf(Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS)))
        add(
            intent(
                id = "manage_all_files",
                title = "All files access",
                subtitle = "Tap → Manage all files permission",
                priority = 4,
                action = Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION,
                "files", "storage", "scoped",
                icon = "privacy",
            )
        )
        add(intent("vr_listeners", "VR helper services", "Tap → VR listener apps", 4, Settings.ACTION_VR_LISTENER_SETTINGS, "vr", "immersive", icon = "privacy", fallbacks = listOf(Settings.ACTION_PRIVACY_SETTINGS)))
        add(intent("condition_providers", "Condition providers", "Tap → Automation / rules access", 4, Settings.ACTION_CONDITION_PROVIDER_SETTINGS, "rules", "automation", icon = "privacy"))
        add(intent("advanced_memory", "Advanced memory protection", "Tap → Memory tagging / hardening", 4, Settings.ACTION_ADVANCED_MEMORY_PROTECTION_SETTINGS, "mte", "memory", "security", icon = "privacy", fallbacks = listOf(Settings.ACTION_PRIVACY_SETTINGS, Settings.ACTION_SECURITY_SETTINGS)))

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
        add(intent("user_dictionary", "Personal dictionary", "Tap → Custom words for keyboard", 5, Settings.ACTION_USER_DICTIONARY_SETTINGS, "dictionary", "keyboard", icon = "personal"))
        add(intent("input_method", "On-screen keyboard", "Tap → Keyboard apps & layout", 5, Settings.ACTION_INPUT_METHOD_SETTINGS, "keyboard", "ime", icon = "device"))
        add(intent("input_subtype", "Keyboard languages", "Tap → Input method subtypes", 5, Settings.ACTION_INPUT_METHOD_SUBTYPE_SETTINGS, "keyboard", "language", "subtype", icon = "device", fallbacks = listOf(Settings.ACTION_INPUT_METHOD_SETTINGS)))
        add(intent("hard_keyboard", "Physical keyboard", "Tap → Hardware keyboard layout", 5, Settings.ACTION_HARD_KEYBOARD_SETTINGS, "keyboard", icon = "device", fallbacks = listOf(Settings.ACTION_INPUT_METHOD_SETTINGS)))
        add(intent("app_locale", "Per-app language", "Tap → Language for each app", 5, Settings.ACTION_APP_LOCALE_SETTINGS, "locale", "language", "app", icon = "device", fallbacks = listOf(Settings.ACTION_LOCALE_SETTINGS)))
        add(intent("regional", "Regional preferences", "Tap → Units, numbers, temperature", 5, Settings.ACTION_REGIONAL_PREFERENCES_SETTINGS, "region", "units", "temperature", icon = "device", fallbacks = listOf(Settings.ACTION_LOCALE_SETTINGS)))

        // ── P6: Network & connectivity ──────────────────────────────────────
        add(intent("wifi", "Wi‑Fi", "Tap → Wireless networks", 6, Settings.ACTION_WIFI_SETTINGS, "wireless", "internet", icon = "network"))
        add(intent("wireless", "Network & internet", "Tap → Mobile data, Wi‑Fi, VPN", 6, Settings.ACTION_WIRELESS_SETTINGS, "data", "sim", icon = "network"))
        add(intent("data_roaming", "Mobile network", "Tap → Cellular & roaming", 6, Settings.ACTION_DATA_ROAMING_SETTINGS, "roaming", "lte", "5g", icon = "network"))
        add(intent("network_operator", "Network operators", "Tap → Preferred mobile network", 6, Settings.ACTION_NETWORK_OPERATOR_SETTINGS, "carrier", "sim", "operator", icon = "network"))
        add(intent("apn", "Access Point Names", "Tap → APN / carrier internet config", 6, Settings.ACTION_APN_SETTINGS, "apn", "carrier", "mms", icon = "network", fallbacks = listOf(Settings.ACTION_DATA_ROAMING_SETTINGS)))
        add(intent("data_usage", "Data usage", "Tap → Mobile & Wi‑Fi data counters", 6, Settings.ACTION_DATA_USAGE_SETTINGS, "data", "usage", "metered", icon = "network", fallbacks = listOf(Settings.ACTION_WIRELESS_SETTINGS)))
        add(intent("data_saver", "Data saver", "Tap → Restrict background data usage", 6, "android.settings.DATA_SAVER_SETTINGS", "data", "saver", icon = "network", fallbacks = listOf(Settings.ACTION_DATA_USAGE_SETTINGS)))
        add(intent("sim_profiles", "SIM profiles", "Tap → Manage eSIM & SIM cards", 6, Settings.ACTION_MANAGE_ALL_SIM_PROFILES_SETTINGS, "esim", "sim", "dual", icon = "network", fallbacks = listOf(Settings.ACTION_NETWORK_OPERATOR_SETTINGS)))
        add(intent("bluetooth", "Bluetooth", "Tap → Paired devices", 6, Settings.ACTION_BLUETOOTH_SETTINGS, "headphones", "speaker", icon = "bluetooth"))
        add(intent("nfc", "NFC", "Tap → Near-field communication", 6, Settings.ACTION_NFC_SETTINGS, "tap", "pay", icon = "network"))
        add(intent("nfc_payment", "Tap & pay", "Tap → NFC payment default", 6, Settings.ACTION_NFC_PAYMENT_SETTINGS, "pay", "wallet", "contactless", icon = "network", fallbacks = listOf(Settings.ACTION_NFC_SETTINGS)))
        add(intent("nfc_sharing", "Android Beam / NFC share", "Tap → NFC content sharing", 6, Settings.ACTION_NFCSHARING_SETTINGS, "beam", "share", icon = "network", fallbacks = listOf(Settings.ACTION_NFC_SETTINGS)))
        add(intent("vpn", "VPN", "Tap → Virtual private networks", 6, Settings.ACTION_VPN_SETTINGS, "vpn", icon = "network"))
        add(intent("tether", "Hotspot & tethering", "Tap → USB / Wi‑Fi hotspot", 6, "android.settings.TETHER_SETTINGS", "hotspot", "usb", icon = "network"))
        add(intent("airplane", "Aeroplane mode", "Tap → Disable all radios", 6, Settings.ACTION_AIRPLANE_MODE_SETTINGS, "flight", icon = "network"))
        add(intent("usb", "USB preferences", "Tap → USB mode when connected", 6, "android.settings.USB_SETTINGS", "usb", "file transfer", "mtp", icon = "network", fallbacks = listOf(Settings.ACTION_WIRELESS_SETTINGS)))
        add(intent("wifi_ip", "Wi‑Fi IP settings", "Tap → Static IP and advanced Wi‑Fi", 6, Settings.ACTION_WIFI_IP_SETTINGS, "ip", "dns", "static", icon = "network", fallbacks = listOf(Settings.ACTION_WIFI_SETTINGS)))
        add(intent("satellite", "Satellite connectivity", "Tap → Emergency / satellite messaging", 6, Settings.ACTION_SATELLITE_SETTING, "satellite", "sos", icon = "network", fallbacks = listOf(Settings.ACTION_WIRELESS_SETTINGS)))
        add(intent("wallet", "Quick Access Wallet", "Tap → Cards & passes on lock screen", 6, Settings.ACTION_QUICK_ACCESS_WALLET_SETTINGS, "wallet", "cards", "passes", icon = "network", fallbacks = listOf(Settings.ACTION_NFC_PAYMENT_SETTINGS)))

        // ── P7: Device ──────────────────────────────────────────────────────
        add(intent("storage", "Storage", "Tap → Free space & app storage", 7, Settings.ACTION_INTERNAL_STORAGE_SETTINGS, "disk", "space", icon = "device"))
        add(intent("memory_card", "Storage volumes", "Tap → SD card & external storage", 7, Settings.ACTION_MEMORY_CARD_SETTINGS, "sd", "external", icon = "device", fallbacks = listOf(Settings.ACTION_INTERNAL_STORAGE_SETTINGS)))
        add(
            intent(
                "storage_volume_access",
                "Storage volume access",
                "Tap → Apps with volume access",
                7,
                "android.settings.STORAGE_VOLUME_ACCESS_SETTINGS",
                "storage", "volume",
                icon = "device",
                fallbacks = listOf(Settings.ACTION_INTERNAL_STORAGE_SETTINGS),
            ),
        )
        add(intent("battery", "Battery saver", "Tap → Battery saver mode", 7, Settings.ACTION_BATTERY_SAVER_SETTINGS, "power", "charge", "saver", icon = "battery"))
        add(intent("battery_usage", "Battery optimization", "Tap → Per-app battery restrictions", 7, Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS, "drain", "doze", icon = "battery"))
        add(
            intent(
                id = "ignore_battery_this_app",
                title = "Battery optimization list",
                subtitle = "Tap → System list of battery-unrestricted apps",
                priority = 7,
                action = Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS,
                "battery", "background", "cowlsly",
                icon = "battery",
            )
        )
        add(intent("date_time", "Date & time", "Tap → Timezone & automatic time", 7, Settings.ACTION_DATE_SETTINGS, "clock", "timezone", icon = "device"))
        add(intent("locale", "Languages", "Tap → System language & region", 7, Settings.ACTION_LOCALE_SETTINGS, "language", icon = "device"))
        add(intent("gestures", "Gestures & navigation", "Tap → System navigation and gestures", 7, "android.settings.GESTURE_NAVIGATION_SETTINGS", "gestures", "navigation", icon = "device", fallbacks = listOf(Settings.ACTION_SETTINGS)))
        add(intent("accessibility", "Accessibility", "Tap → TalkBack, captions, display size", 7, Settings.ACTION_ACCESSIBILITY_SETTINGS, "talkback", "a11y", icon = "accessibility"))
        add(intent("captioning", "Captions", "Tap → Subtitle preferences", 7, Settings.ACTION_CAPTIONING_SETTINGS, "subtitles", icon = "accessibility", fallbacks = listOf(Settings.ACTION_ACCESSIBILITY_SETTINGS)))
        add(intent("print", "Printing", "Tap → Print services & printers", 7, Settings.ACTION_PRINT_SETTINGS, "print", "printer", icon = "device"))
        add(intent("search_settings", "System search", "Tap → Search engine & indexing", 7, Settings.ACTION_SEARCH_SETTINGS, "search", "index", icon = "device"))
        add(intent("app_search", "App search settings", "Tap → On-device app search", 7, Settings.ACTION_APP_SEARCH_SETTINGS, "search", "apps", icon = "device", fallbacks = listOf(Settings.ACTION_SEARCH_SETTINGS)))
        add(intent("device_info", "About phone", "Tap → Model, Android version, legal", 7, Settings.ACTION_DEVICE_INFO_SETTINGS, "phone", "build", "version", icon = "device"))
        add(intent("regulatory", "Regulatory labels", "Tap → Safety & regulatory info", 7, Settings.ACTION_SHOW_REGULATORY_INFO, "regulatory", "safety", "legal", icon = "device", fallbacks = listOf(Settings.ACTION_DEVICE_INFO_SETTINGS)))
        add(intent("work_policy", "Work policy info", "Tap → Managed device policy", 7, Settings.ACTION_SHOW_WORK_POLICY_INFO, "work", "enterprise", "mdm", icon = "device", fallbacks = listOf(Settings.ACTION_SECURITY_SETTINGS)))
        add(intent("android_settings_root", "All system settings", "Tap → Full Android Settings app", 7, Settings.ACTION_SETTINGS, "settings", "system", icon = "device"))

        // ── P8: Apps ────────────────────────────────────────────────────────
        add(intent("applications", "All apps", "Tap → Installed applications", 8, Settings.ACTION_APPLICATION_SETTINGS, "apps", "installed", icon = "apps"))
        add(intent("manage_apps", "App management", "Tap → Force stop, storage, defaults", 8, Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS, "apps", icon = "apps"))
        add(intent("unused_apps", "Unused apps", "Tap → Apps you haven't opened lately", 8, "android.settings.MANAGE_UNUSED_APPS", "unused", "archive", icon = "apps", fallbacks = listOf(Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS)))
        add(intent("default_apps", "Default apps", "Tap → Browser, phone, SMS defaults", 8, Settings.ACTION_MANAGE_DEFAULT_APPS_SETTINGS, "defaults", icon = "apps"))
        add(
            intent(
                id = "unknown_sources",
                title = "Install unknown apps",
                subtitle = "Tap → Sideload permission per app",
                priority = 8,
                action = Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES,
                "sideload", "apk",
                icon = "apps",
                packageData = true,
            )
        )
        add(
            intent(
                id = "write_settings",
                title = "Modify system settings",
                subtitle = "Tap → Apps that can change settings",
                priority = 8,
                action = Settings.ACTION_MANAGE_WRITE_SETTINGS,
                "write settings",
                icon = "apps",
                packageData = true,
            )
        )
        add(intent("notification_listener", "Notification access", "Tap → Apps reading notifications", 8, Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS, "notifications", icon = "notifications"))
        add(intent("picture_in_picture", "Picture-in-picture", "Tap → PiP allowed apps", 8, "android.settings.PICTURE_IN_PICTURE_SETTINGS", "pip", icon = "apps", fallbacks = listOf(Settings.ACTION_APPLICATION_SETTINGS)))
        add(intent("app_usage_access", "App usage access", "Tap → Which apps may read usage", 8, Settings.ACTION_USAGE_ACCESS_SETTINGS, "usage", "stats", icon = "apps"))
        add(intent("app_usage_settings", "App usage dashboard", "Tap → Screen time style usage", 8, Settings.ACTION_APP_USAGE_SETTINGS, "screen time", "usage", icon = "apps", fallbacks = listOf(Settings.ACTION_USAGE_ACCESS_SETTINGS)))
        add(intent("webview", "WebView implementation", "Tap → System WebView provider", 8, Settings.ACTION_WEBVIEW_SETTINGS, "webview", "chrome", icon = "apps"))
        add(
            intent(
                id = "app_details_this",
                title = "This app details",
                subtitle = "Tap → Simple Settings app info",
                priority = 8,
                action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                "app info", "permissions", "storage",
                icon = "apps",
                packageData = true,
            )
        )
        add(intent("open_by_default", "Open by default", "Tap → Link handling defaults", 8, Settings.ACTION_APP_OPEN_BY_DEFAULT_SETTINGS, "links", "verified", "default", icon = "apps", packageData = true, fallbacks = listOf(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)))
        add(intent("full_screen_intent", "Full-screen intents", "Tap → Alarm-style full screen apps", 8, Settings.ACTION_MANAGE_APP_USE_FULL_SCREEN_INTENT, "fullscreen", "alarm", icon = "apps", fallbacks = listOf("android.settings.SPECIAL_ACCESS_SETTINGS")))
        add(intent("manage_media", "Manage media access", "Tap → Media management apps", 8, Settings.ACTION_REQUEST_MANAGE_MEDIA, "media", "photos", icon = "apps", packageData = true, fallbacks = listOf(Settings.ACTION_APPLICATION_SETTINGS)))
        add(intent("background_data", "Background data", "Tap → Unrestricted background data", 8, Settings.ACTION_IGNORE_BACKGROUND_DATA_RESTRICTIONS_SETTINGS, "background", "data", icon = "apps", packageData = true, fallbacks = listOf(Settings.ACTION_DATA_USAGE_SETTINGS)))

        // ── P9: Notifications ─────────────────────────────────────────────
        add(intent("notification_settings", "Notifications", "Tap → App notification channels", 9, Settings.ACTION_ALL_APPS_NOTIFICATION_SETTINGS, "alerts", "channels", icon = "notifications", fallbacks = listOf("android.settings.ALL_APPS_NOTIFICATION_SETTINGS")))
        add(intent("notification_policy", "DND access", "Tap → Apps controlling Do Not Disturb", 9, Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS, "dnd", icon = "notifications"))
        add(intent("channel_notification", "Notification history", "Tap → Recent notification log", 9, "android.settings.NOTIFICATION_HISTORY_SETTINGS", "history", icon = "notifications", fallbacks = listOf(Settings.ACTION_ALL_APPS_NOTIFICATION_SETTINGS)))
        add(intent("notification_assistant", "Notification assistant", "Tap → Smart reply & ranking", 9, Settings.ACTION_NOTIFICATION_ASSISTANT_SETTINGS, "assistant", "smart", icon = "notifications"))
        add(intent("bubble_settings", "Bubbles", "Tap → Conversation bubble apps", 9, "android.settings.NOTIFICATION_BUBBLE_SETTINGS", "bubbles", "chat", icon = "notifications", fallbacks = listOf(Settings.ACTION_ALL_APPS_NOTIFICATION_SETTINGS)))
        add(
            SettingsEntry(
                id = "app_notification_this",
                title = "This app notifications",
                subtitle = "Tap → Simple Settings notification channels",
                keywords = listOf("channels", "alerts", "notifications"),
                basePriority = 9,
                panelType = SettingsPanelType.SYSTEM_INTENT,
                intentAction = Settings.ACTION_APP_NOTIFICATION_SETTINGS,
                intentExtraKey = Settings.EXTRA_APP_PACKAGE,
                // value filled with package name at launch
                intentExtraValue = null,
                iconKey = "notifications",
            )
        )

        // ── P10: Accounts & backup ──────────────────────────────────────────
        add(
            SettingsEntry(
                id = "cowlsly_account",
                title = "Cowlsly account sync",
                subtitle = "Add Cowlsly to device Accounts — self-syncing with website & apps",
                keywords = listOf("cowlsly", "account", "sync", "login", "website"),
                basePriority = 10,
                panelType = SettingsPanelType.COWLSLY_ACCOUNT,
                iconKey = "cowlsly",
            )
        )
        add(intent("sync", "Accounts & sync", "Tap → Google & other accounts", 10, Settings.ACTION_SYNC_SETTINGS, "google", "account", icon = "account"))
        add(intent("backup", "Backup", "Tap → Google backup & restore", 10, "android.settings.BACKUP_AND_RESET_SETTINGS", "restore", icon = "account", fallbacks = listOf(Settings.ACTION_PRIVACY_SETTINGS)))
        add(intent("add_account", "Add account", "Tap → Sign in to services", 10, Settings.ACTION_ADD_ACCOUNT, "sign in", icon = "account", fallbacks = listOf("android.settings.ADD_ACCOUNT_SETTINGS", Settings.ACTION_SYNC_SETTINGS)))
        add(intent("user_settings", "Users", "Tap → Multiple users & profiles", 10, "android.settings.USER_SETTINGS", "users", "profile", "guest", icon = "account"))

        // ── P11: Developer (gated) — every common dev screen, all clickable ─
        add(
            SettingsEntry(
                id = "developer_gate",
                title = "Developer tools",
                subtitle = "PIN required — opens Android Developer options",
                keywords = listOf("developer", "debug", "adb", "usb"),
                basePriority = 11,
                panelType = SettingsPanelType.DEVELOPER_GATE,
                requiresDeveloperAccess = true,
                requiresPin = true,
                iconKey = "developer",
                intentAction = Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS,
                componentPackage = "com.android.settings",
                componentClass = "com.android.settings.Settings\$DevelopmentSettingsDashboardActivity",
            )
        )
        add(
            SettingsEntry(
                id = "devops_tools",
                title = "DevOps & suite pipeline",
                subtitle = "Cowlsly Console CI, sync, and deploy tools",
                keywords = listOf("devops", "ci", "cd", "pipeline", "deploy", "sync", "console"),
                basePriority = 11,
                panelType = SettingsPanelType.DEVOPS_TOOLS,
                requiresDeveloperAccess = true,
                iconKey = "devops",
            )
        )
        add(
            devIntent(
                id = "dev_options",
                title = "Developer options",
                subtitle = "Tap → Full Android developer settings",
                action = Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS,
                "debug", "developer", "options",
                icon = "developer",
                componentPackage = "com.android.settings",
                componentClass = "com.android.settings.Settings\$DevelopmentSettingsDashboardActivity",
            )
        )
        add(
            devIntent(
                id = "adb_enabled",
                title = "USB debugging",
                subtitle = "Tap → Dev options (toggle USB debugging there)",
                action = Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS,
                "adb", "usb", "debugging",
                icon = "developer",
                componentPackage = "com.android.settings",
                componentClass = "com.android.settings.Settings\$DevelopmentSettingsDashboardActivity",
            )
        )
        add(
            devIntent(
                id = "wireless_debug",
                title = "Wireless debugging",
                subtitle = "Tap → ADB over Wi‑Fi",
                action = "android.settings.WIRELESS_DEBUGGING_SETTINGS",
                "adb", "wifi debug", "wireless",
                icon = "developer",
                fallbacks = listOf(Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS),
                componentPackage = "com.android.settings",
                componentClass = "com.android.settings.Settings\$WirelessDebuggingActivity",
            )
        )
        add(
            devIntent(
                id = "developer_graphics",
                title = "Graphics / GPU debug",
                subtitle = "Tap → GPU rendering & HW overlays (in Dev options)",
                action = Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS,
                "gpu", "graphics", "hwui", "profile",
                icon = "developer",
            )
        )
        add(
            devIntent(
                id = "running_services",
                title = "Running services",
                subtitle = "Tap → Active processes & RAM",
                action = "android.settings.RUNNING_SERVICES",
                "ram", "process", "services",
                icon = "developer",
                fallbacks = listOf(Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS),
                componentPackage = "com.android.settings",
                componentClass = "com.android.settings.Settings\$RunningServicesActivity",
            )
        )
        add(
            devIntent(
                id = "dev_quick_settings",
                title = "Quick settings developer tiles",
                subtitle = "Tap → Dev options (tiles section)",
                action = Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS,
                "tiles", "qs", "quick settings",
                icon = "developer",
            )
        )
        add(
            devIntent(
                id = "dev_stay_awake",
                title = "Stay awake while charging",
                subtitle = "Tap → Dev options (Stay awake toggle)",
                action = Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS,
                "stay awake", "charging", "screen",
                icon = "developer",
            )
        )
        add(
            devIntent(
                id = "dev_oem_unlock",
                title = "OEM unlocking",
                subtitle = "Tap → Dev options (bootloader unlock gate)",
                action = Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS,
                "oem", "unlock", "bootloader", "fastboot",
                icon = "developer",
            )
        )
        add(
            devIntent(
                id = "dev_usb_config",
                title = "Default USB configuration",
                subtitle = "Tap → Dev options (USB mode default)",
                action = Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS,
                "usb", "mtp", "ptp", "midi",
                icon = "developer",
                fallbacks = listOf("android.settings.USB_SETTINGS"),
            )
        )
        add(
            devIntent(
                id = "dev_wifi_verbose",
                title = "Wi‑Fi verbose logging",
                subtitle = "Tap → Dev options (networking debug)",
                action = Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS,
                "wifi", "verbose", "logging",
                icon = "developer",
            )
        )
        add(
            devIntent(
                id = "dev_mobile_data_always",
                title = "Mobile data always active",
                subtitle = "Tap → Dev options (radio debug)",
                action = Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS,
                "mobile data", "radio", "always active",
                icon = "developer",
            )
        )
        add(
            devIntent(
                id = "dev_strict_mode",
                title = "Strict mode enabled",
                subtitle = "Tap → Dev options (flash on main-thread violations)",
                action = Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS,
                "strict mode", "anr", "jank",
                icon = "developer",
            )
        )
        add(
            devIntent(
                id = "dev_pointer_location",
                title = "Pointer location",
                subtitle = "Tap → Dev options (touch trail overlay)",
                action = Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS,
                "pointer", "touch", "location",
                icon = "developer",
            )
        )
        add(
            devIntent(
                id = "dev_show_taps",
                title = "Show taps",
                subtitle = "Tap → Dev options (visual tap feedback)",
                action = Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS,
                "taps", "touch", "feedback",
                icon = "developer",
            )
        )
        add(
            devIntent(
                id = "dev_show_layout",
                title = "Show layout bounds",
                subtitle = "Tap → Dev options (clip bounds overlay)",
                action = Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS,
                "layout", "bounds", "debug draw",
                icon = "developer",
            )
        )
        add(
            devIntent(
                id = "dev_force_rtl",
                title = "Force RTL layout",
                subtitle = "Tap → Dev options (right-to-left UI test)",
                action = Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS,
                "rtl", "layout", "locale",
                icon = "developer",
            )
        )
        add(
            devIntent(
                id = "dev_animation_scale",
                title = "Animator duration scale",
                subtitle = "Tap → Dev options (window / transition / animator)",
                action = Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS,
                "animation", "scale", "0.5x", "off",
                icon = "developer",
            )
        )
        add(
            devIntent(
                id = "dev_force_gpu",
                title = "Force GPU rendering",
                subtitle = "Tap → Dev options (hardware accelerate)",
                action = Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS,
                "gpu", "force", "rendering",
                icon = "developer",
            )
        )
        add(
            devIntent(
                id = "dev_debug_gpu_overdraw",
                title = "Debug GPU overdraw",
                subtitle = "Tap → Dev options (overdraw colours)",
                action = Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS,
                "overdraw", "gpu", "debug",
                icon = "developer",
            )
        )
        add(
            devIntent(
                id = "dev_dont_keep_activities",
                title = "Don't keep activities",
                subtitle = "Tap → Dev options (destroy every activity)",
                action = Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS,
                "activities", "lifecycle", "destroy",
                icon = "developer",
            )
        )
        add(
            devIntent(
                id = "dev_background_process_limit",
                title = "Background process limit",
                subtitle = "Tap → Dev options (process limit)",
                action = Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS,
                "process", "limit", "background",
                icon = "developer",
            )
        )
        add(
            devIntent(
                id = "dev_demo_mode",
                title = "System UI demo mode",
                subtitle = "Tap → Dev options (clean status bar for screenshots)",
                action = Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS,
                "demo", "screenshot", "status bar",
                icon = "developer",
            )
        )
        add(
            devIntent(
                id = "dev_feature_flags",
                title = "Feature flags",
                subtitle = "Tap → Dev options (experimental flags)",
                action = Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS,
                "feature flags", "experimental",
                icon = "developer",
            )
        )
        add(
            devIntent(
                id = "dev_standby_apps",
                title = "Standby apps",
                subtitle = "Tap → Dev options (app standby buckets)",
                action = Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS,
                "standby", "bucket", "doze",
                icon = "developer",
            )
        )
        add(
            devIntent(
                id = "dev_background_check",
                title = "Background check",
                subtitle = "Tap → Dev options (apps running in background)",
                action = Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS,
                "background", "check",
                icon = "developer",
            )
        )
        add(
            devIntent(
                id = "dev_inactive_apps",
                title = "Inactive apps",
                subtitle = "Tap → Dev options (force app inactive)",
                action = Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS,
                "inactive", "force stop",
                icon = "developer",
            )
        )
        add(
            devIntent(
                id = "dev_select_gpu",
                title = "Select GPU renderer / driver",
                subtitle = "Tap → Graphics driver preferences",
                action = "android.settings.DEVELOPMENT_SETTINGS",
                "gpu", "driver", "angle",
                icon = "developer",
                fallbacks = listOf(Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS),
            )
        )
        add(devIntent("dev_dream", "Developer screensaver link", "Tap → Screensaver / Daydream", Settings.ACTION_DREAM_SETTINGS, "screensaver", icon = "developer"))

        // ── Hidden device apps & privileged tools ───────────────────────────
        add(
            SettingsEntry(
                id = "hidden_device_apps",
                title = "Hidden device apps",
                subtitle = "Obscure Android settings & hidden activities — each row is tappable",
                keywords = listOf("hidden", "obscure", "activity launcher", "secret", "device apps"),
                basePriority = 11,
                panelType = SettingsPanelType.HIDDEN_DEVICE_APPS,
                requiresDeveloperAccess = true,
                iconKey = "hidden",
            )
        )
        add(
            SettingsEntry(
                id = "shizuku_status",
                title = "Shizuku",
                subtitle = "Privileged API for hidden settings",
                keywords = listOf("shizuku", "root", "shell", "privileged", "hidden"),
                basePriority = 11,
                panelType = SettingsPanelType.SHIZUKU_TOOL,
                requiresDeveloperAccess = true,
                iconKey = "shizuku",
            )
        )
        add(
            devIntent(
                id = "appops",
                title = "App ops",
                subtitle = "Tap → Hidden per-permission toggles",
                action = "android.settings.APP_OPS_SETTINGS",
                "appops", "hidden",
                icon = "hidden",
                fallbacks = listOf(Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS),
                componentPackage = "com.android.settings",
                componentClass = "com.android.settings.Settings\$AppOpsSummaryActivity",
            )
        )
        add(devIntent("locale_secret", "Regional preferences (dev)", "Tap → Advanced locale overrides", Settings.ACTION_REGIONAL_PREFERENCES_SETTINGS, "locale", "region", icon = "device", fallbacks = listOf(Settings.ACTION_LOCALE_SETTINGS)))
        add(
            devIntent(
                id = "device_admin",
                title = "Device admin apps",
                subtitle = "Tap → Enterprise & admin controllers",
                action = "android.settings.DEVICE_ADMIN_SETTINGS",
                "admin", "enterprise",
                icon = "hidden",
                componentPackage = "com.android.settings",
                componentClass = "com.android.settings.Settings\$DeviceAdminSettingsActivity",
            )
        )
        add(devIntent("ignore_battery", "Battery optimization (dev)", "Tap → Per-app battery restrictions", Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS, "battery", "doze", icon = "battery"))
        add(
            intent(
                id = "request_schedule_exact_alarm",
                title = "Exact alarms",
                subtitle = "Tap → Alarms & reminders special access",
                priority = 11,
                action = Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM,
                "alarm", "exact",
                icon = "hidden",
                packageData = true,
                fallbacks = listOf("android.settings.SPECIAL_ACCESS_SETTINGS"),
                requiresDeveloperAccess = true,
            )
        )

        // ── P12: About & sync ───────────────────────────────────────────────
        add(
            SettingsEntry(
                id = "about",
                title = "About Simple Settings",
                subtitle = "Version, grant developer access, export sync JSON",
                keywords = listOf("version", "about", "cowlsly", "sync"),
                basePriority = 12,
                panelType = SettingsPanelType.ABOUT,
                iconKey = "about",
            )
        )
        add(intent("tips_support", "Tips & support", "Tap → System settings (help entry points)", 12, Settings.ACTION_SETTINGS, "help", "feedback", "support", icon = "about"))
        add(
            SettingsEntry(
                id = "credits",
                title = "Credits",
                subtitle = "Fair credit to external app makers we learn from",
                keywords = listOf("credits", "acknowledgements", "thanks", "shizuku", "hidden settings"),
                basePriority = 13,
                panelType = SettingsPanelType.CREDITS,
                iconKey = "credits",
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
        icon: String? = null,
        packageData: Boolean = false,
        dataUri: String? = null,
        extraKey: String? = null,
        extraValue: String? = null,
        fallbacks: List<String> = emptyList(),
        componentPackage: String? = null,
        componentClass: String? = null,
        requiresDeveloperAccess: Boolean = false,
    ): SettingsEntry = SettingsEntry(
        id = id,
        title = title,
        subtitle = subtitle,
        keywords = keywords.toList(),
        basePriority = priority,
        panelType = SettingsPanelType.SYSTEM_INTENT,
        intentAction = action,
        intentPackageData = packageData,
        intentDataUri = dataUri,
        intentExtraKey = extraKey,
        intentExtraValue = extraValue,
        fallbackActions = fallbacks,
        componentPackage = componentPackage,
        componentClass = componentClass,
        requiresDeveloperAccess = requiresDeveloperAccess,
        iconKey = icon,
    )

    private fun devIntent(
        id: String,
        title: String,
        subtitle: String,
        action: String,
        vararg keywords: String,
        icon: String? = "developer",
        fallbacks: List<String> = emptyList(),
        componentPackage: String? = null,
        componentClass: String? = null,
    ): SettingsEntry = SettingsEntry(
        id = id,
        title = title,
        subtitle = subtitle,
        keywords = keywords.toList(),
        basePriority = 11,
        panelType = SettingsPanelType.SYSTEM_INTENT,
        intentAction = action,
        requiresDeveloperAccess = true,
        fallbackActions = fallbacks,
        componentPackage = componentPackage,
        componentClass = componentClass,
        iconKey = icon,
    )
}
