package com.cowlsly.simplesettings.sync

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.permissionDataStore: DataStore<Preferences> by preferencesDataStore("permission_promotions")

class PermissionManager(private val context: Context) {
    private val store = context.permissionDataStore

    val promotionsAcknowledged: Flow<Set<String>> = store.data.map { prefs ->
        prefs.asMap().mapNotNull { (key, value) ->
            if (key.name.startsWith("ack_") && value == true) key.name.removePrefix("ack_") else null
        }.toSet()
    }

    fun canWriteSystemSettings(): Boolean = Settings.System.canWrite(context)

    fun openWriteSettingsGrant() {
        val intent = Intent(
            Settings.ACTION_MANAGE_WRITE_SETTINGS,
            Uri.parse("package:${context.packageName}"),
        ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    suspend fun acknowledgePromotion(permissionId: String) {
        store.edit { it[booleanPreferencesKey("ack_$permissionId")] = true }
    }

    suspend fun needsPromotion(permissionId: String, acknowledged: Set<String>): Boolean =
        permissionId !in acknowledged
}