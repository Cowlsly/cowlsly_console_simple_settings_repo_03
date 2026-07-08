package com.cowlsly.simplesettings

/**
 * Cowlsly Console web settings — the suite home on cowlsly.com.
 * Phase 5 will sync via [WEBSITE_SETTINGS_SYNC_FILE].
 */
object CowlslyConsole {
    const val SETTINGS_URL = "https://cowlsly.com/console/settings"
    const val SIGNUP_URL = "https://cowlsly.com/account/create"
    const val WEBSITE_SETTINGS_SYNC_FILE = "cowlsly_website_settings_sync.json"
    const val SYNC_PULL_URL = "https://cowlsly.com/api/console/settings/sync"
    const val SYNC_PUSH_URL = "https://cowlsly.com/api/console/settings/sync"
    const val SYNC_BROADCAST_ACTION = "com.cowlsly.simplesettings.SYNC_UPDATED"
    const val ANDROID_SYNC_SETTINGS = "android.settings.SYNC_SETTINGS"
    const val SPOTIFY_PACKAGE = "com.spotify.music"
}