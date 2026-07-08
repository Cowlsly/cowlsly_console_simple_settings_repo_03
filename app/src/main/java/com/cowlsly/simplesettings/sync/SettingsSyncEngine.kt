package com.cowlsly.simplesettings.sync

import android.content.Context
import android.media.AudioManager
import android.provider.Settings
import com.cowlsly.simplesettings.data.SettingsRepository
import com.cowlsly.simplesettings.data.UserPreferences
import com.cowlsly.simplesettings.ui.panels.VOLUME_STEPS
import kotlinx.coroutines.flow.first

class SettingsSyncEngine(
    private val context: Context,
    private val repository: SettingsRepository,
) {
    private val resolver = context.contentResolver
    private val audioManager = context.getSystemService(AudioManager::class.java)

    suspend fun syncEntry(entryId: String): SyncState {
        val binding = SettingsSyncRegistry.bindingFor(entryId)
        return when (binding.source) {
            SyncSource.LOCAL -> syncLocal(entryId, binding)
            SyncSource.SYSTEM -> syncSystemKey(entryId, binding)
            SyncSource.SECURE -> syncSecureKey(entryId, binding)
            SyncSource.GLOBAL -> syncGlobalKey(entryId, binding)
            SyncSource.AUDIO -> syncAudio(entryId, binding)
            SyncSource.INTENT, SyncSource.SHIZUKU -> SyncState(
                entryId = entryId,
                displayValue = "Controlled in system app",
                control = SyncControl.READ_ONLY,
                synced = true,
                lastSyncedAt = System.currentTimeMillis(),
                sourceLabel = "Android Settings",
            )
        }
    }

    suspend fun syncAllCatalogIds(ids: List<String>): Map<String, SyncState> =
        ids.associateWith { syncEntry(it) }

    /** Pull live system values into local prefs where we own FULL control. */
    suspend fun pullIntoPreferences(): UserPreferences {
        syncAudioToPrefs()
        syncBrightnessToPrefs()
        return repository.preferences.first()
    }

    fun pushVolumeToSystem(stepIndex: Int, muted: Boolean) {
        val max = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        if (muted) {
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0)
        } else {
            val pct = VOLUME_STEPS[stepIndex.coerceIn(0, VOLUME_STEPS.lastIndex)]
            val vol = (max * (pct / 100f)).toInt().coerceIn(0, max)
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, vol, 0)
        }
    }

    suspend fun pushBrightness(percent: Float) {
        if (!Settings.System.canWrite(context)) return
        val value = (percent.coerceIn(0f, 1f) * 255).toInt()
        Settings.System.putInt(resolver, Settings.System.SCREEN_BRIGHTNESS, value)
    }

    private suspend fun syncLocal(entryId: String, binding: SyncBinding): SyncState {
        val prefs = repository.preferences.first()
        val value = when (entryId) {
            "panel_tint" -> "${(prefs.panelTint * 100).toInt()}% tint"
            "lock_on_background" -> if (prefs.lockOnBackground) "On" else "Off"
            "casmea_entry" -> if (prefs.casmeaFullName.isBlank()) "Not set" else prefs.casmeaFullName
            else -> "Saved locally"
        }
        return SyncState(
            entryId = entryId,
            displayValue = value,
            control = binding.control,
            synced = true,
            lastSyncedAt = System.currentTimeMillis(),
            sourceLabel = "Simple Settings",
        )
    }

    private fun syncSystemKey(entryId: String, binding: SyncBinding): SyncState {
        val key = binding.key ?: return emptyState(entryId)
        val raw = try {
            Settings.System.getInt(resolver, key).toString()
        } catch (_: Exception) {
            "Unavailable"
        }
        val display = format(binding, raw, key)
        return SyncState(
            entryId = entryId,
            displayValue = display,
            control = binding.control,
            synced = raw != "Unavailable",
            lastSyncedAt = System.currentTimeMillis(),
            sourceLabel = "Settings.System",
        )
    }

    private fun syncSecureKey(entryId: String, binding: SyncBinding): SyncState {
        val key = binding.key ?: return emptyState(entryId)
        val raw = try {
            Settings.Secure.getInt(resolver, key).toString()
        } catch (_: Exception) {
            "Unavailable"
        }
        return SyncState(
            entryId = entryId,
            displayValue = format(binding, raw, key),
            control = binding.control,
            synced = raw != "Unavailable",
            lastSyncedAt = System.currentTimeMillis(),
            sourceLabel = "Settings.Secure",
        )
    }

    private fun syncGlobalKey(entryId: String, binding: SyncBinding): SyncState {
        val key = binding.key ?: return emptyState(entryId)
        val raw = try {
            Settings.Global.getInt(resolver, key).toString()
        } catch (_: Exception) {
            "Unavailable"
        }
        return SyncState(
            entryId = entryId,
            displayValue = format(binding, raw, key),
            control = binding.control,
            synced = raw != "Unavailable",
            lastSyncedAt = System.currentTimeMillis(),
            sourceLabel = "Settings.Global",
        )
    }

    private suspend fun syncAudio(entryId: String, binding: SyncBinding): SyncState {
        val max = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        val current = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
        val pct = if (max == 0) 0 else (current * 100 / max)
        val step = VOLUME_STEPS.withIndex().minByOrNull { kotlin.math.abs(it.value - pct) }?.index ?: 0
        repository.updateVolumeFromSystem(step, current == 0)
        return SyncState(
            entryId = entryId,
            displayValue = if (current == 0) "Muted" else "$pct% media",
            control = binding.control,
            synced = true,
            lastSyncedAt = System.currentTimeMillis(),
            sourceLabel = "AudioManager",
        )
    }

    private suspend fun syncAudioToPrefs() {
        val state = syncAudio("volume", SettingsSyncRegistry.bindingFor("volume")!!)
        // values already written in syncAudio
    }

    private suspend fun syncBrightnessToPrefs() {
        try {
            val raw = Settings.System.getInt(resolver, Settings.System.SCREEN_BRIGHTNESS)
            // brightness stored in system only for now; panel uses quick link
        } catch (_: Exception) { }
    }

    private fun format(binding: SyncBinding, raw: String, key: String): String {
        if (raw == "Unavailable") return raw
        binding.valueFormatter?.let { return it(raw) }
        return when (key) {
            Settings.System.SCREEN_BRIGHTNESS -> {
                val pct = raw.toIntOrNull()?.let { (it * 100 / 255) } ?: 0
                "$pct% brightness"
            }
            Settings.Secure.LOCATION_MODE -> when (raw) {
                "0" -> "Off"
                "1" -> "Device only"
                "2" -> "Battery saving"
                "3" -> "High accuracy"
                else -> raw
            }
            else -> raw
        }
    }

    private fun emptyState(entryId: String) = SyncState(
        entryId = entryId,
        displayValue = "—",
        synced = false,
        sourceLabel = "Unknown",
    )
}