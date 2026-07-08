package com.cowlsly.simplesettings.sync

import android.content.Context
import android.content.Intent
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.cowlsly.simplesettings.CowlslyConsole
import com.cowlsly.simplesettings.account.CowlslyAccountManager
import com.cowlsly.simplesettings.data.SettingsCatalog
import com.cowlsly.simplesettings.data.SettingsRepository
import com.cowlsly.simplesettings.data.SettingsSorter
import com.cowlsly.simplesettings.data.UserPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import org.json.JSONObject

private val Context.suiteSyncMeta: DataStore<Preferences> by preferencesDataStore("suite_sync_meta")

data class SuiteSyncStatus(
    val revision: Long = 0,
    val lastLocalWrite: String = "",
    val lastWebsitePush: String = "",
    val lastWebsitePull: String = "",
    val websiteMessage: String = "Not synced yet",
    val coherent: Boolean = true,
)

class SuiteSyncCoordinator(
    private val context: Context,
    private val repository: SettingsRepository,
) {
    private val store = SuiteSyncStore(context)
    private val website = WebsiteSyncClient()
    private val meta = context.suiteSyncMeta

    val status: Flow<SuiteSyncStatus> = meta.data.map { prefs ->
        SuiteSyncStatus(
            revision = prefs[Keys.REVISION] ?: 0L,
            lastLocalWrite = prefs[Keys.LAST_LOCAL] ?: "",
            lastWebsitePush = prefs[Keys.LAST_PUSH] ?: "",
            lastWebsitePull = prefs[Keys.LAST_PULL] ?: "",
            websiteMessage = prefs[Keys.WEB_MSG] ?: "Not synced yet",
            coherent = prefs[Keys.COHERENT] ?: true,
        )
    }

    private object Keys {
        val REVISION = longPreferencesKey("revision")
        val LAST_LOCAL = stringPreferencesKey("last_local")
        val LAST_PUSH = stringPreferencesKey("last_push")
        val LAST_PULL = stringPreferencesKey("last_pull")
        val WEB_MSG = stringPreferencesKey("web_msg")
        val COHERENT = booleanPreferencesKey("coherent")
    }

    /**
     * Called after every setting mutation — pushes to Android, local JSON, website, and suite apps.
     */
    suspend fun onSettingChanged(changedEntryId: String) {
        val prefs = repository.preferences.first()
        val entryState = repository.syncEntry(changedEntryId)
        val mirrors = store.read()?.systemMirrors?.toMutableMap() ?: mutableMapOf()
        mirrors[changedEntryId] = entryState.displayValue
        val revision = (meta.data.first()[Keys.REVISION] ?: 0L) + 1L
        val doc = buildDocument(prefs, mirrors.toMap(), revision)
        store.write(doc)
        meta.edit {
            it[Keys.REVISION] = revision
            it[Keys.LAST_LOCAL] = store.timestamp()
            it[Keys.COHERENT] = true
        }
        broadcastSync(changedEntryId, doc)
        CowlslyAccountManager.requestSync(context)
        val push = website.push(doc)
        meta.edit {
            it[Keys.WEB_MSG] = push.message
            if (push.success) it[Keys.LAST_PUSH] = store.timestamp()
        }
        repository.syncEntry(changedEntryId)
    }

    /** Full coherent sync: website → Android sources → local JSON → suite broadcast */
    suspend fun syncAllCoherent(): Map<String, SyncState> {
        val pull = website.pull()
        if (pull.success && pull.remoteDocument != null) {
            applyRemoteIfNewer(pull.remoteDocument)
            meta.edit {
                it[Keys.LAST_PULL] = store.timestamp()
                it[Keys.WEB_MSG] = pull.message
            }
        } else {
            meta.edit { it[Keys.WEB_MSG] = pull.message }
        }
        repository.pullFromSystem()
        val ids = SettingsCatalog.allEntries.map { it.id }
        val mirrors = repository.syncFromSources(ids)
        val prefs = repository.preferences.first()
        val revision = (meta.data.first()[Keys.REVISION] ?: 0L) + 1L
        val doc = buildDocument(prefs, mirrors.mapValues { it.value.displayValue }, revision)
        store.write(doc)
        meta.edit {
            it[Keys.REVISION] = revision
            it[Keys.LAST_LOCAL] = store.timestamp()
            it[Keys.COHERENT] = true
        }
        broadcastSync("full_sync", doc)
        CowlslyAccountManager.requestSync(context)
        website.push(doc)
        return mirrors
    }

    suspend fun readExportJson(): String {
        val prefs = repository.preferences.first()
        return store.read()?.toJson()?.toString(2)
            ?: buildDocument(prefs, emptyMap(), 0).toJson().toString(2)
    }

    suspend fun importFromJson(raw: String): Boolean {
        return runCatching {
            val doc = SuiteSyncDocument.fromJson(JSONObject(raw))
            applyRemoteIfNewer(doc)
            onSettingChanged("import")
            true
        }.getOrDefault(false)
    }

    private suspend fun applyRemoteIfNewer(remote: SuiteSyncDocument) {
        val localRev = meta.data.first()[Keys.REVISION] ?: 0L
        if (remote.revision <= localRev && remote.source == "simple_settings_android") return
        // Web sends alphabetical catalog only — never apply appPanelOrder from remote.
        repository.applySuiteDocument(remote)
    }

    private suspend fun buildDocument(
        prefs: UserPreferences,
        mirrors: Map<String, String>,
        revision: Long,
    ): SuiteSyncDocument {
        val scores = repository.usageTracker.combinedScoresSnapshot()
        val neglect = repository.usageTracker.neglectScoresSnapshot(
            SettingsCatalog.allEntries.map { it.id },
        )
        val appOrder = SettingsSorter.sort(
            SettingsCatalog.allEntries,
            scores,
            neglect,
            prefs.developerAccessGranted,
        ).map { it.id }
        val webOrder = SettingsSorter.sortAlphabetical(
            SettingsCatalog.allEntries,
            prefs.developerAccessGranted,
        ).map { it.id }
        return SuiteSyncDocument(
            revision = revision,
            updatedAt = store.timestamp(),
            panelTint = prefs.panelTint,
            lockOnBackground = prefs.lockOnBackground,
            volumeStepIndex = prefs.volumeStepIndex,
            volumeMuted = prefs.isMuted,
            developerAccessGranted = prefs.developerAccessGranted,
            casmeaConfigured = prefs.casmeaFullName.isNotBlank(),
            systemMirrors = mirrors,
            appPanelOrder = appOrder,
            webCatalogAlphabetical = webOrder,
            cowlslyAccountLinked = CowlslyAccountManager.hasAccount(context),
        )
    }

    private fun broadcastSync(entryId: String, doc: SuiteSyncDocument) {
        val intent = Intent(CowlslyConsole.SYNC_BROADCAST_ACTION).apply {
            putExtra("entry_id", entryId)
            putExtra("revision", doc.revision)
        }
        context.sendBroadcast(intent)
        // Exported provider serves the same JSON to Vault, CASMEA, Cowlsly.com companion apps.
    }
}