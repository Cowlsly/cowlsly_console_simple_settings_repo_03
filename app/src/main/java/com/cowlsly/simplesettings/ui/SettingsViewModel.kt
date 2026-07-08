package com.cowlsly.simplesettings.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.cowlsly.simplesettings.data.SettingsCatalog
import com.cowlsly.simplesettings.data.SettingsEntry
import com.cowlsly.simplesettings.data.SettingsRankHint
import com.cowlsly.simplesettings.data.SettingsRepository
import com.cowlsly.simplesettings.data.SettingsSorter
import com.cowlsly.simplesettings.data.UserPreferences
import com.cowlsly.simplesettings.sync.PermissionCatalog
import com.cowlsly.simplesettings.sync.PermissionInstruction
import com.cowlsly.simplesettings.sync.SuiteSyncStatus
import com.cowlsly.simplesettings.sync.SyncState
import com.cowlsly.simplesettings.shizuku.ShizukuHelper
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class SettingsUiState(
    val preferences: UserPreferences = UserPreferences(),
    val pages: List<List<SettingsEntry>> = emptyList(),
    val filteredEntries: List<SettingsEntry> = emptyList(),
    val currentPage: Int = 0,
    val searchQuery: String = "",
    val isSearchActive: Boolean = false,
    val pinDialogEntry: SettingsEntry? = null,
    val pinInput: String = "",
    val pinError: Boolean = false,
    val syncStates: Map<String, SyncState> = emptyMap(),
    val pendingPermission: PermissionInstruction? = null,
    val suiteSyncStatus: SuiteSyncStatus = SuiteSyncStatus(),
    val pinUnlockedIds: Set<String> = emptySet(),
    val lockRequired: Boolean = false,
    val rankHints: Map<String, SettingsRankHint> = emptyMap(),
)

class SettingsViewModel(private val repository: SettingsRepository) : ViewModel() {
    private val _currentPage = MutableStateFlow(0)
    private val _searchQuery = MutableStateFlow("")
    private val _pinDialogEntry = MutableStateFlow<SettingsEntry?>(null)
    private val _pinInput = MutableStateFlow("")
    private val _pinError = MutableStateFlow(false)
    private val _syncStates = MutableStateFlow<Map<String, SyncState>>(emptyMap())
    private val _pendingPermission = MutableStateFlow<PermissionInstruction?>(null)
    private val _pinUnlockedIds = MutableStateFlow<Set<String>>(emptySet())
    private val _lockRequired = MutableStateFlow(false)
    private var searchJob: Job? = null

    private val sortedEntries = combine(
        repository.preferences,
        repository.usageTracker.combinedScores(),
        repository.usageTracker.neglectScores(SettingsCatalog.allEntries.map { it.id }),
    ) { prefs, scores, neglect ->
        SettingsSorter.sort(SettingsCatalog.allEntries, scores, neglect, prefs.developerAccessGranted)
    }

    private val rankHints = combine(
        sortedEntries,
        repository.usageTracker.combinedScores(),
        repository.usageTracker.neglectScores(SettingsCatalog.allEntries.map { it.id }),
    ) { sorted, scores, neglect ->
        SettingsSorter.rankHints(sorted, scores, neglect)
    }

    private val coreState = combine(
        repository.preferences,
        repository.pagedSettings,
        sortedEntries,
        _currentPage,
        _searchQuery,
    ) { prefs, pages, sorted, page, query ->
        val filtered = if (query.isBlank()) emptyList() else sorted.filter { it.matchesQuery(query) }
        SettingsUiState(
            preferences = prefs,
            pages = pages,
            filteredEntries = filtered,
            currentPage = page.coerceIn(0, (pages.size - 1).coerceAtLeast(0)),
            searchQuery = query,
            isSearchActive = query.isNotBlank(),
        )
    }

    private val pinBundle = combine(
        _pinDialogEntry,
        _pinInput,
        _pinError,
        _pinUnlockedIds,
        _lockRequired,
    ) { pinEntry, pinInput, pinError, unlocked, lockRequired ->
        PinBundle(pinEntry, pinInput, pinError, unlocked, lockRequired)
    }

    private val extendedState = combine(
        coreState,
        _syncStates,
        repository.suiteSync.status,
        pinBundle,
        rankHints,
    ) { base, syncStates, suiteStatus, pin, hints ->
        base.copy(
            pinDialogEntry = pin.pinEntry,
            pinInput = pin.pinInput,
            pinError = pin.pinError,
            syncStates = syncStates,
            pinUnlockedIds = pin.unlocked,
            lockRequired = pin.lockRequired,
            suiteSyncStatus = suiteStatus,
            rankHints = hints,
        )
    }

    private data class PinBundle(
        val pinEntry: SettingsEntry?,
        val pinInput: String,
        val pinError: Boolean,
        val unlocked: Set<String>,
        val lockRequired: Boolean,
    )

    val uiState: StateFlow<SettingsUiState> = combine(
        extendedState,
        _pendingPermission,
    ) { state, pendingPermission ->
        state.copy(pendingPermission = pendingPermission)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), SettingsUiState())

    init {
        viewModelScope.launch {
            syncAllCoherent()
            promoteNextPermission()
        }
    }

    fun syncAllCoherent() {
        viewModelScope.launch {
            _syncStates.value = repository.syncAllCoherent()
        }
    }

    fun syncEntry(entryId: String) {
        viewModelScope.launch {
            val state = repository.syncEntry(entryId)
            _syncStates.value = _syncStates.value + (entryId to state)
        }
    }

    fun onAppBackgrounded() {
        viewModelScope.launch {
            val prefs = repository.preferences.first()
            if (prefs.lockOnBackground && prefs.developerPinSet) {
                _pinUnlockedIds.value = emptySet()
                _lockRequired.value = true
            }
        }
    }

    fun onAppForegrounded() {
        if (!_lockRequired.value) return
    }

    fun clearLockAfterPin() {
        _lockRequired.value = false
    }

    fun isEntryUnlocked(entry: SettingsEntry): Boolean {
        val needsPin = entry.requiresPin || entry.requiresDeveloperAccess
        return !needsPin || entry.id in uiState.value.pinUnlockedIds || entry.panelType == com.cowlsly.simplesettings.data.SettingsPanelType.ABOUT
    }

    fun requestPinFor(entry: SettingsEntry) {
        _pinDialogEntry.value = entry
        _pinInput.value = ""
        _pinError.value = false
    }

    fun promoteNextPermission() {
        viewModelScope.launch {
            val acked = repository.permissionManager.promotionsAcknowledged.first()
            val next = PermissionCatalog.all.firstOrNull { perm ->
                repository.permissionManager.needsPromotion(perm.id, acked) &&
                    when (perm.id) {
                        "write_settings" -> !repository.permissionManager.canWriteSystemSettings()
                        "shizuku" -> {
                            val s = ShizukuHelper.status()
                            !(s.binderAvailable && s.permissionGranted)
                        }
                        "account_sync" -> !repository.hasCowlslyAccount()
                        else -> true
                    }
            }
            _pendingPermission.value = next
        }
    }

    fun allowPendingPermission() {
        viewModelScope.launch {
            val perm = _pendingPermission.value ?: return@launch
            when (perm.id) {
                "write_settings" -> repository.permissionManager.openWriteSettingsGrant()
                "shizuku" -> ShizukuHelper.requestPermission(1001)
            }
            repository.permissionManager.acknowledgePromotion(perm.id)
            _pendingPermission.value = null
            promoteNextPermission()
            syncAllCoherent()
        }
    }

    fun dismissPendingPermission() {
        viewModelScope.launch {
            val perm = _pendingPermission.value ?: return@launch
            repository.permissionManager.acknowledgePromotion(perm.id)
            _pendingPermission.value = null
            promoteNextPermission()
        }
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
        searchJob?.cancel()
        if (query.isNotBlank()) {
            searchJob = viewModelScope.launch {
                delay(400)
                recordSearchHits(query)
            }
        }
    }

    fun nextPage() {
        val max = uiState.value.pages.size - 1
        if (_currentPage.value < max) _currentPage.value++
    }

    fun previousPage() {
        if (_currentPage.value > 0) _currentPage.value--
    }

    /** Count a panel view toward most-used ranking (no PIN prompt). */
    fun recordPanelView(entry: SettingsEntry) {
        viewModelScope.launch { repository.usageTracker.recordOpen(entry.id) }
    }

    /** Explicit open action — records usage and may request PIN for protected rows. */
    fun onEntryOpened(entry: SettingsEntry) {
        recordPanelView(entry)
        if (entry.requiresPin && entry.id !in _pinUnlockedIds.value) {
            requestPinFor(entry)
        }
    }

    fun updatePinInput(value: String) {
        _pinInput.value = value.filter { it.isDigit() }.take(8)
        _pinError.value = false
    }

    fun dismissPinDialog() {
        _pinDialogEntry.value = null
        _pinInput.value = ""
        _pinError.value = false
    }

    fun submitPin(onUnlocked: (SettingsEntry) -> Unit) {
        viewModelScope.launch {
            val entry = _pinDialogEntry.value ?: return@launch
            if (uiState.value.lockRequired) {
                val ok = repository.checkPin(_pinInput.value)
                if (ok) {
                    dismissPinDialog()
                    clearLockAfterPin()
                } else {
                    _pinError.value = true
                }
                return@launch
            }
            val ok = repository.checkPin(_pinInput.value)
            if (ok) {
                _pinUnlockedIds.value = _pinUnlockedIds.value + entry.id
                dismissPinDialog()
                onUnlocked(entry)
            } else {
                _pinError.value = true
            }
        }
    }

    fun setVolumeStep(index: Int) = viewModelScope.launch { repository.setVolumeStep(index) }
    fun setMuted(muted: Boolean) = viewModelScope.launch { repository.setMuted(muted) }
    fun setPanelTint(tint: Float) = viewModelScope.launch { repository.setPanelTint(tint) }
    fun setLockOnBackground(enabled: Boolean) = viewModelScope.launch { repository.setLockOnBackground(enabled) }
    fun grantDeveloperAccess(pin: String) = viewModelScope.launch { repository.grantDeveloperAccess(pin) }

    fun exportSyncJson(onReady: (String) -> Unit) {
        viewModelScope.launch { onReady(repository.suiteSync.readExportJson()) }
    }

    fun importSyncJson(raw: String, onDone: (Boolean) -> Unit) {
        viewModelScope.launch {
            val ok = repository.suiteSync.importFromJson(raw)
            if (ok) syncAllCoherent()
            onDone(ok)
        }
    }

    private suspend fun recordSearchHits(query: String) {
        val prefs = repository.preferences.first()
        val scores = repository.usageTracker.combinedScoresSnapshot()
        val neglect = repository.usageTracker.neglectScoresSnapshot(SettingsCatalog.allEntries.map { it.id })
        SettingsSorter.sort(SettingsCatalog.allEntries, scores, neglect, prefs.developerAccessGranted)
            .filter { it.matchesQuery(query) }
            .forEach { repository.usageTracker.recordSearch(it.id) }
    }

    fun saveCasmea(
        name: String,
        blood: String,
        allergies: String,
        medications: String,
        contact: String,
        conditions: String,
    ) = viewModelScope.launch {
        repository.updateCasmea(name, blood, allergies, medications, contact, conditions)
    }

    class Factory(private val repository: SettingsRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            SettingsViewModel(repository) as T
    }
}