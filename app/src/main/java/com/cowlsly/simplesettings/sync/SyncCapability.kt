package com.cowlsly.simplesettings.sync

enum class SyncSource {
    /** Suite-local preference mirrored to consumers (CASMEA, Cowlsly.com JSON). */
    LOCAL,
    /** android.provider.Settings.System */
    SYSTEM,
    /** android.provider.Settings.Secure */
    SECURE,
    /** android.provider.Settings.Global */
    GLOBAL,
    /** AudioManager / platform APIs */
    AUDIO,
    /** Opens system UI; read-only mirror when key exists */
    INTENT,
    /** Privileged write via Shizuku when granted */
    SHIZUKU,
}

enum class SyncControl {
    /** Read and write stay in sync with the original source */
    FULL,
    /** Read live value; user edits in system screen */
    READ_ONLY,
    /** Local only; exported via content provider / website JSON */
    LOCAL_EXPORT,
}

data class SyncState(
    val entryId: String,
    val displayValue: String,
    val lastSyncedAt: Long = 0L,
    val control: SyncControl = SyncControl.READ_ONLY,
    val synced: Boolean = false,
    val sourceLabel: String = "",
)

data class PermissionInstruction(
    val id: String,
    val title: String,
    val why: String,
    val steps: String,
    val requiredFor: List<String>,
)