package com.cowlsly.simplesettings.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
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

    suspend fun recordOpen(entryId: String) {
        val key = intPreferencesKey("open_$entryId")
        dataStore.edit { prefs ->
            prefs[key] = (prefs[key] ?: 0) + 1
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

    suspend fun combinedScoresSnapshot(): Map<String, Int> = combinedScores().first()
}