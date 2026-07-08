package com.cowlsly.simplesettings.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.usageDataStore: DataStore<Preferences> by preferencesDataStore("usage_tracker")

class UsageTracker(private val context: Context) {
    private val dataStore = context.usageDataStore

    fun usageScores(): Flow<Map<String, Int>> = dataStore.data.map { prefs ->
        prefs.asMap().mapNotNull { (key, value) ->
            if (key.name.startsWith("open_") && value is Int) {
                key.name.removePrefix("open_") to value
            } else null
        }.toMap()
    }

    fun searchScores(): Flow<Map<String, Int>> = dataStore.data.map { prefs ->
        prefs.asMap().mapNotNull { (key, value) ->
            if (key.name.startsWith("search_") && value is Int) {
                key.name.removePrefix("search_") to value
            } else null
        }.toMap()
    }

    fun lastOpenTimestamps(): Flow<Map<String, Long>> = dataStore.data.map { prefs ->
        prefs.asMap().mapNotNull { (key, value) ->
            if (key.name.startsWith("last_open_") && value is Long) {
                key.name.removePrefix("last_open_") to value
            } else null
        }.toMap()
    }

    suspend fun recordOpen(entryId: String) {
        val openKey = intPreferencesKey("open_$entryId")
        val lastKey = longPreferencesKey("last_open_$entryId")
        dataStore.edit { prefs ->
            prefs[openKey] = (prefs[openKey] ?: 0) + 1
            prefs[lastKey] = System.currentTimeMillis()
        }
    }

    suspend fun recordSearch(entryId: String) {
        val key = intPreferencesKey("search_$entryId")
        dataStore.edit { prefs ->
            prefs[key] = (prefs[key] ?: 0) + 1
        }
    }

    fun combinedScores(): Flow<Map<String, Int>> = dataStore.data.map { prefs ->
        val ids = mutableSetOf<String>()
        prefs.asMap().keys.forEach { key ->
            when {
                key.name.startsWith("open_") -> ids.add(key.name.removePrefix("open_"))
                key.name.startsWith("search_") -> ids.add(key.name.removePrefix("search_"))
            }
        }
        ids.associateWith { id ->
            val opens = prefs[intPreferencesKey("open_$id")] ?: 0
            val searches = prefs[intPreferencesKey("search_$id")] ?: 0
            opens * 3 + searches * 2
        }
    }

    /**
     * Neglect score — higher means the setting has been missed longer and should float closer.
     * Never-opened entries receive a stable baseline so they gently surface over time.
     */
    fun neglectScores(entryIds: Collection<String>): Flow<Map<String, Int>> =
        lastOpenTimestamps().map { lastOpens ->
            val now = System.currentTimeMillis()
            val dayMs = 86_400_000L
            entryIds.associateWith { id ->
                val last = lastOpens[id]
                if (last == null) {
                    28 + (id.hashCode() and 0x7).coerceAtLeast(0)
                } else {
                    val days = ((now - last) / dayMs).toInt().coerceAtLeast(0)
                    (days * 3).coerceAtMost(72)
                }
            }
        }

    suspend fun combinedScoresSnapshot(): Map<String, Int> = combinedScores().first()

    suspend fun neglectScoresSnapshot(entryIds: Collection<String>): Map<String, Int> =
        neglectScores(entryIds).first()
}