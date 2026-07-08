package com.cowlsly.simplesettings.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.cowlsly.simplesettings.account.CowlslyAccountManager
import com.cowlsly.simplesettings.sync.PermissionManager
import com.cowlsly.simplesettings.sync.SettingsSyncEngine
import com.cowlsly.simplesettings.sync.SuiteSyncCoordinator
import com.cowlsly.simplesettings.sync.SuiteSyncDocument
import com.cowlsly.simplesettings.sync.SyncState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.settingsDataStore: DataStore<Preferences> by preferencesDataStore("simple_settings")

data class UserPreferences(
    val volumeStepIndex: Int = 2,
    val isMuted: Boolean = false,
    val panelTint: Float = 0.5f,
    val lockOnBackground: Boolean = true,
    val developerAccessGranted: Boolean = false,
    val developerPinSet: Boolean = false,
    val casmeaFullName: String = "",
    val casmeaBloodType: String = "",
    val casmeaAllergies: String = "",
    val casmeaMedications: String = "",
    val casmeaEmergencyContact: String = "",
    val casmeaConditions: String = "",
)

class SettingsRepository(context: Context) {
    private val appContext = context.applicationContext
    private val dataStore = appContext.settingsDataStore
    private val securePrefs = SecurePrefs(appContext)
    val usageTracker = UsageTracker(appContext)
    val permissionManager = PermissionManager(appContext)
    val syncEngine = SettingsSyncEngine(appContext, this)
    val suiteSync: SuiteSyncCoordinator = SuiteSyncCoordinator(appContext, this)

    private object Keys {
        val VOLUME_STEP = intPreferencesKey("volume_step")
        val MUTED = booleanPreferencesKey("muted")
        val PANEL_TINT = floatPreferencesKey("panel_tint")
        val LOCK_BG = booleanPreferencesKey("lock_on_background")
        val DEV_ACCESS = booleanPreferencesKey("developer_access_granted")
    }

    val preferences: Flow<UserPreferences> = dataStore.data.map { prefs ->
        UserPreferences(
            volumeStepIndex = prefs[Keys.VOLUME_STEP] ?: 2,
            isMuted = prefs[Keys.MUTED] ?: false,
            panelTint = prefs[Keys.PANEL_TINT] ?: 0.5f,
            lockOnBackground = prefs[Keys.LOCK_BG] ?: true,
            developerAccessGranted = prefs[Keys.DEV_ACCESS] ?: false,
            developerPinSet = securePrefs.hasPin(),
            casmeaFullName = securePrefs.getCasmeaField("name"),
            casmeaBloodType = securePrefs.getCasmeaField("blood"),
            casmeaAllergies = securePrefs.getCasmeaField("allergies"),
            casmeaMedications = securePrefs.getCasmeaField("meds"),
            casmeaEmergencyContact = securePrefs.getCasmeaField("contact"),
            casmeaConditions = securePrefs.getCasmeaField("conditions"),
        )
    }

    val pagedSettings: Flow<List<List<SettingsEntry>>> = combine(
        preferences,
        usageTracker.combinedScores(),
    ) { prefs, scores ->
        val sorted = SettingsSorter.sort(
            SettingsCatalog.allEntries,
            scores,
            prefs.developerAccessGranted,
        )
        SettingsSorter.paginate(sorted, SettingsCatalog.PAGE_SIZE)
    }

    private suspend fun afterChange(entryId: String) {
        suiteSync.onSettingChanged(entryId)
    }

    suspend fun setVolumeStep(index: Int) {
        val step = index.coerceIn(0, 4)
        val muted = preferences.first().isMuted
        dataStore.edit { it[Keys.VOLUME_STEP] = step }
        syncEngine.pushVolumeToSystem(step, muted)
        afterChange("volume")
    }

    suspend fun setMuted(muted: Boolean) {
        val step = preferences.first().volumeStepIndex
        dataStore.edit { it[Keys.MUTED] = muted }
        syncEngine.pushVolumeToSystem(step, muted)
        afterChange("volume")
    }

    suspend fun pullFromSystem() {
        syncEngine.pullIntoPreferences()
    }

    suspend fun syncFromSources(entryIds: List<String>): Map<String, SyncState> {
        pullFromSystem()
        return syncEngine.syncAllCatalogIds(entryIds)
    }

    suspend fun syncAllCoherent(): Map<String, SyncState> = suiteSync.syncAllCoherent()

    suspend fun syncEntry(entryId: String): SyncState = syncEngine.syncEntry(entryId)

    suspend fun updateVolumeFromSystem(stepIndex: Int, muted: Boolean) {
        dataStore.edit {
            it[Keys.VOLUME_STEP] = stepIndex.coerceIn(0, 4)
            it[Keys.MUTED] = muted
        }
    }

    suspend fun setPanelTint(tint: Float) {
        dataStore.edit { it[Keys.PANEL_TINT] = tint.coerceIn(0f, 1f) }
        afterChange("panel_tint")
    }

    suspend fun setLockOnBackground(enabled: Boolean) {
        dataStore.edit { it[Keys.LOCK_BG] = enabled }
        afterChange("lock_on_background")
    }

    suspend fun grantDeveloperAccess(pin: String): Boolean {
        if (pin.length < 4) return false
        securePrefs.setPin(pin)
        dataStore.edit { it[Keys.DEV_ACCESS] = true }
        afterChange("about")
        return true
    }

    suspend fun checkPin(pin: String): Boolean {
        val stored = securePrefs.getPin() ?: return false
        return stored == pin
    }

    suspend fun updateCasmea(
        name: String,
        blood: String,
        allergies: String,
        medications: String,
        contact: String,
        conditions: String,
    ) {
        securePrefs.setCasmeaFields(
            mapOf(
                "name" to name,
                "blood" to blood,
                "allergies" to allergies,
                "meds" to medications,
                "contact" to contact,
                "conditions" to conditions,
            ),
        )
        afterChange("casmea_entry")
    }

    fun hasCowlslyAccount(): Boolean = CowlslyAccountManager.hasAccount(appContext)

    suspend fun applySuiteDocument(doc: SuiteSyncDocument) {
        dataStore.edit {
            it[Keys.PANEL_TINT] = doc.panelTint.coerceIn(0f, 1f)
            it[Keys.LOCK_BG] = doc.lockOnBackground
            it[Keys.VOLUME_STEP] = doc.volumeStepIndex.coerceIn(0, 4)
            it[Keys.MUTED] = doc.volumeMuted
            it[Keys.DEV_ACCESS] = doc.developerAccessGranted
        }
        syncEngine.pushVolumeToSystem(doc.volumeStepIndex, doc.volumeMuted)
    }
}