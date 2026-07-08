package com.cowlsly.simplesettings.ui

import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cowlsly.simplesettings.R
import com.cowlsly.simplesettings.audio.SoundEffects
import com.cowlsly.simplesettings.data.SettingsEntry
import com.cowlsly.simplesettings.data.SettingsPanelType
import com.cowlsly.simplesettings.data.SettingsRankHint
import com.cowlsly.simplesettings.data.SettingsRepository
import com.cowlsly.simplesettings.data.SystemIntentLauncher
import com.cowlsly.simplesettings.ui.components.CogsBackground
import com.cowlsly.simplesettings.ui.components.CowlslyConsoleSettingsButton
import com.cowlsly.simplesettings.ui.components.GlassPanel
import com.cowlsly.simplesettings.ui.components.PageTurnerButtons
import com.cowlsly.simplesettings.ui.components.PermissionPromoterCard
import com.cowlsly.simplesettings.ui.components.SettingsPanelHeader
import com.cowlsly.simplesettings.ui.components.SyncStatusRow
import com.cowlsly.simplesettings.ui.panels.CasmeaPanel
import com.cowlsly.simplesettings.ui.panels.CowlslyAccountPanel
import com.cowlsly.simplesettings.ui.panels.CreditsPanel
import com.cowlsly.simplesettings.ui.panels.DevOpsToolsPanel
import com.cowlsly.simplesettings.ui.panels.HiddenDeviceAppsPanel
import com.cowlsly.simplesettings.ui.panels.ShizukuPanel
import com.cowlsly.simplesettings.ui.panels.SpotifyDefaultPanel
import com.cowlsly.simplesettings.ui.panels.VolumePanel
import com.cowlsly.simplesettings.ui.theme.CowlslyCyan
import com.cowlsly.simplesettings.ui.theme.CowlslyGoldLight
import com.cowlsly.simplesettings.ui.theme.LocalPanelTint

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleSettingsRoot(repository: SettingsRepository) {
    val viewModel: SettingsViewModel = viewModel(factory = SettingsViewModel.Factory(repository))
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val soundEffects = remember { SoundEffects(context) }

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> {
                    viewModel.onAppForegrounded()
                    viewModel.syncAllCoherent()
                    viewModel.promoteNextPermission()
                }
                Lifecycle.Event.ON_PAUSE -> viewModel.onAppBackgrounded()
                else -> Unit
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            soundEffects.release()
        }
    }

    CompositionLocalProvider(LocalPanelTint provides state.preferences.panelTint) {
    Box(modifier = Modifier.fillMaxSize()) {
        CogsBackground()
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = {
                        Column {
                            Text("Simple Settings", color = CowlslyGoldLight)
                            Text(
                                "Rev ${state.suiteSyncStatus.revision} · ${state.suiteSyncStatus.websiteMessage}",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.White.copy(alpha = 0.7f),
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
                )
            },
            bottomBar = {
                if (!state.isSearchActive && state.pages.isNotEmpty()) {
                    PageTurnerButtons(
                        currentPage = state.currentPage,
                        totalPages = state.pages.size,
                        onPrevious = viewModel::previousPage,
                        onNext = viewModel::nextPage,
                        soundEffects = soundEffects,
                    )
                }
            },
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 16.dp),
            ) {
                OutlinedTextField(
                    value = state.searchQuery,
                    onValueChange = viewModel::setSearchQuery,
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Search any setting…") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    singleLine = true,
                )
                Spacer(modifier = Modifier.height(12.dp))

                if (!state.isSearchActive && state.currentPage == 0) {
                    CowlslyConsoleSettingsButton(soundEffects = soundEffects)
                    Spacer(modifier = Modifier.height(12.dp))
                    state.pendingPermission?.let { perm ->
                        PermissionPromoterCard(
                            instruction = perm,
                            onAllow = viewModel::allowPendingPermission,
                            onDismiss = viewModel::dismissPendingPermission,
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }

                if (state.isSearchActive) {
                    SearchResultsList(
                        entries = state.filteredEntries,
                        state = state,
                        viewModel = viewModel,
                        onLaunchIntent = { entry -> launchSystemIntent(context, entry, soundEffects) },
                    )
                } else {
                    val pageEntries = state.pages.getOrNull(state.currentPage).orEmpty()
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(bottom = 16.dp),
                    ) {
                        items(pageEntries, key = { it.id }) { entry ->
                            SettingsEntryPanel(
                                entry = entry,
                                state = state,
                                viewModel = viewModel,
                                onLaunchIntent = { launchSystemIntent(context, entry, soundEffects) },
                            )
                        }
                    }
                }
            }
        }

        if (state.pinDialogEntry != null || state.lockRequired) {
            PinDialog(
                entry = state.pinDialogEntry,
                lockRequired = state.lockRequired,
                pinInput = state.pinInput,
                pinError = state.pinError,
                onPinChange = viewModel::updatePinInput,
                onDismiss = viewModel::dismissPinDialog,
                onSubmit = {
                    viewModel.submitPin { entry ->
                        when (entry.panelType) {
                            SettingsPanelType.DEVELOPER_GATE ->
                                launchSystemIntent(context, entry, soundEffects)
                            SettingsPanelType.SYSTEM_INTENT ->
                                launchSystemIntent(context, entry, soundEffects)
                            SettingsPanelType.CASMEA_ENTRY -> Unit
                            else -> Unit
                        }
                    }
                },
            )
        }
    }
    }
}

@Composable
private fun SearchResultsList(
    entries: List<SettingsEntry>,
    state: com.cowlsly.simplesettings.ui.SettingsUiState,
    viewModel: SettingsViewModel,
    onLaunchIntent: (SettingsEntry) -> Unit,
) {
    if (entries.isEmpty()) {
        Text("No settings match your search.", modifier = Modifier.padding(16.dp))
        return
    }
    LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        items(entries, key = { it.id }) { entry ->
            SettingsEntryPanel(
                entry = entry,
                state = state,
                viewModel = viewModel,
                onLaunchIntent = { onLaunchIntent(entry) },
            )
        }
    }
}

@Composable
private fun SettingsEntryPanel(
    entry: SettingsEntry,
    state: com.cowlsly.simplesettings.ui.SettingsUiState,
    viewModel: SettingsViewModel,
    onLaunchIntent: () -> Unit,
) {
    val unlocked = viewModel.isEntryUnlocked(entry)
    val syncState = state.syncStates[entry.id]
    val rankHint = state.rankHints[entry.id] ?: SettingsRankHint.NORMAL
    GlassPanel(rankHint = rankHint) {
        SyncStatusRow(
            syncState = syncState,
            onRefresh = { viewModel.syncEntry(entry.id) },
        )
        Spacer(modifier = Modifier.height(8.dp))
        if (!unlocked) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                SettingsPanelHeader(entry = entry, rankHint = rankHint)
                Text("Enter PIN to view and control this setting.", style = MaterialTheme.typography.bodySmall)
                TextButton(onClick = {
                    viewModel.recordPanelView(entry)
                    viewModel.requestPinFor(entry)
                }) {
                    Text("Unlock")
                }
            }
            return@GlassPanel
        }
        when (entry.panelType) {
            SettingsPanelType.VOLUME -> VolumePanel(
                entry = entry,
                rankHint = rankHint,
                stepIndex = state.preferences.volumeStepIndex,
                isMuted = state.preferences.isMuted,
                onStepSelected = {
                    viewModel.recordPanelView(entry)
                    viewModel.setVolumeStep(it)
                },
                onMuteToggled = {
                    viewModel.recordPanelView(entry)
                    viewModel.setMuted(it)
                },
            )
            SettingsPanelType.PANEL_TINT -> {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    SettingsPanelHeader(entry = entry, rankHint = rankHint)
                    Slider(
                        value = state.preferences.panelTint,
                        onValueChange = viewModel::setPanelTint,
                        onValueChangeFinished = { viewModel.recordPanelView(entry) },
                    )
                }
            }
            SettingsPanelType.CASMEA_ENTRY -> CasmeaPanel(
                entry = entry,
                rankHint = rankHint,
                prefs = state.preferences,
                onSave = { name, blood, allergies, meds, contact, conditions ->
                    viewModel.recordPanelView(entry)
                    viewModel.saveCasmea(name, blood, allergies, meds, contact, conditions)
                },
            )
            SettingsPanelType.IN_APP_TOGGLE -> {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    SettingsPanelHeader(entry = entry, rankHint = rankHint)
                    RowToggle(
                        title = entry.title,
                        subtitle = entry.subtitle,
                        checked = state.preferences.lockOnBackground,
                        onCheckedChange = {
                            viewModel.recordPanelView(entry)
                            viewModel.setLockOnBackground(it)
                        },
                        showTitle = false,
                    )
                }
            }
            SettingsPanelType.DEVELOPER_GATE -> DeveloperGatePanel(
                entry = entry,
                rankHint = rankHint,
                prefs = state.preferences,
                onOpen = {
                    viewModel.onEntryOpened(entry)
                    // After PIN unlock, onEntryOpened may only request PIN; launch
                    // when already unlocked (or PIN just cleared for this session).
                    if (viewModel.isEntryUnlocked(entry)) {
                        onLaunchIntent()
                    }
                },
                onGrantAccess = viewModel::grantDeveloperAccess,
            )
            SettingsPanelType.SHIZUKU_TOOL -> ShizukuPanel(
                entry = entry,
                rankHint = rankHint,
                onRequestPermission = {
                    viewModel.recordPanelView(entry)
                    viewModel.syncAllCoherent()
                    viewModel.promoteNextPermission()
                },
            )
            SettingsPanelType.COWLSLY_ACCOUNT -> CowlslyAccountPanel(
                entry = entry,
                rankHint = rankHint,
                onAccountLinked = {
                    viewModel.recordPanelView(entry)
                    viewModel.syncAllCoherent()
                },
            )
            SettingsPanelType.SPOTIFY_DEFAULT -> SpotifyDefaultPanel(
                entry = entry,
                rankHint = rankHint,
                onOpened = { viewModel.recordPanelView(entry) },
            )
            SettingsPanelType.HIDDEN_DEVICE_APPS -> HiddenDeviceAppsPanel(
                entry = entry,
                onOpened = { viewModel.recordPanelView(entry) },
            )
            SettingsPanelType.DEVOPS_TOOLS -> {
                val panelContext = LocalContext.current
                DevOpsToolsPanel(
                    entry = entry,
                    onExportSync = {
                        viewModel.recordPanelView(entry)
                        viewModel.exportSyncJson { json ->
                            val share = Intent(Intent.ACTION_SEND).apply {
                                type = "application/json"
                                putExtra(Intent.EXTRA_TEXT, json)
                            }
                            panelContext.startActivity(Intent.createChooser(share, "Export sync JSON"))
                        }
                    },
                )
            }
            SettingsPanelType.CREDITS -> CreditsPanel(entry = entry, rankHint = rankHint)
            SettingsPanelType.ABOUT -> AboutPanel(
                entry = entry,
                rankHint = rankHint,
                prefs = state.preferences,
                suiteStatus = state.suiteSyncStatus,
                onGrantDeveloper = viewModel::grantDeveloperAccess,
                onExport = viewModel::exportSyncJson,
                onImport = { raw, done -> viewModel.importSyncJson(raw, done) },
            )
            SettingsPanelType.SYSTEM_INTENT -> {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    SettingsPanelHeader(entry = entry, rankHint = rankHint)
                    Text(
                        "How to open: tap the button below — Android Settings opens for this control.",
                        style = MaterialTheme.typography.bodySmall,
                    )
                    Button(
                        onClick = {
                            viewModel.onEntryOpened(entry)
                            viewModel.syncEntry(entry.id)
                            // If still locked, PIN dialog will launch after unlock.
                            if (viewModel.isEntryUnlocked(entry)) onLaunchIntent()
                        },
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text(stringResource(R.string.open_system_control))
                        Spacer(modifier = Modifier.size(8.dp))
                        Icon(
                            Icons.Default.ChevronRight,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun RowToggle(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    showTitle: Boolean = true,
) {
    androidx.compose.foundation.layout.Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (showTitle) {
            Column(modifier = Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.titleMedium)
                Text(subtitle, style = MaterialTheme.typography.bodySmall)
            }
        } else {
            Spacer(modifier = Modifier.weight(1f))
        }
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}

@Composable
private fun DeveloperGatePanel(
    entry: SettingsEntry,
    rankHint: SettingsRankHint,
    prefs: com.cowlsly.simplesettings.data.UserPreferences,
    onOpen: () -> Unit,
    onGrantAccess: (String) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        SettingsPanelHeader(entry = entry, rankHint = rankHint)
        Text(
            if (prefs.developerAccessGranted) {
                "How to open: enter PIN when asked, then Android Developer options opens. " +
                    "Every developer row below is tappable the same way."
            } else {
                "How to open: grant developer access in About (set a PIN), enable Build number ×7 " +
                    "in About phone, then return here and tap Open."
            },
            style = MaterialTheme.typography.bodySmall,
        )
        Button(
            onClick = onOpen,
            enabled = prefs.developerAccessGranted,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Icon(Icons.Default.Settings, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.size(8.dp))
            Text("Open Developer options")
            Spacer(modifier = Modifier.size(8.dp))
            Icon(Icons.Default.ChevronRight, contentDescription = null, modifier = Modifier.size(18.dp))
        }
    }
}

@Composable
private fun AboutPanel(
    entry: SettingsEntry,
    rankHint: SettingsRankHint,
    prefs: com.cowlsly.simplesettings.data.UserPreferences,
    suiteStatus: com.cowlsly.simplesettings.sync.SuiteSyncStatus,
    onGrantDeveloper: (String) -> Unit,
    onExport: (onReady: (String) -> Unit) -> Unit,
    onImport: (String, (Boolean) -> Unit) -> Unit,
) {
    val context = LocalContext.current
    var pin by remember { mutableStateOf("") }
    var importJson by remember { mutableStateOf("") }
    var importResult by remember { mutableStateOf<String?>(null) }
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        SettingsPanelHeader(entry = entry, rankHint = rankHint)
        Text("Version 1.0.0 — Cowlsly Console suite", style = MaterialTheme.typography.bodySmall)
        Text(
            "Suite sync rev ${suiteStatus.revision} — website, apps, and phone stay coherent.",
            style = MaterialTheme.typography.bodySmall,
            color = CowlslyCyan,
        )
        Text(
            "To unlock developer pages: tap Build number ×7 in About phone, then set a PIN below.",
            style = MaterialTheme.typography.bodySmall,
        )
        OutlinedTextField(
            value = pin,
            onValueChange = { pin = it.filter { c -> c.isDigit() }.take(8) },
            label = { Text("Set developer PIN") },
            modifier = Modifier.fillMaxWidth(),
        )
        TextButton(
            onClick = { if (pin.length >= 4) onGrantDeveloper(pin) },
            enabled = pin.length >= 4,
        ) {
            Text("Grant developer access")
        }
        TextButton(
            onClick = {
                context.startActivity(Intent(Settings.ACTION_DEVICE_INFO_SETTINGS).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
            },
        ) {
            Text("Open About phone (Build number)")
        }
        Text(
            "Developer access: ${if (prefs.developerAccessGranted) "Granted" else "Not granted"}",
            style = MaterialTheme.typography.labelMedium,
            color = CowlslyCyan,
        )
        Text(
            stringResource(R.string.ordering_app_usage),
            style = MaterialTheme.typography.labelSmall,
        )
        Text(
            stringResource(R.string.ordering_web_alpha),
            style = MaterialTheme.typography.labelSmall,
            color = CowlslyCyan,
        )
        TextButton(onClick = {
            onExport { json ->
                val share = Intent(Intent.ACTION_SEND).apply {
                    type = "application/json"
                    putExtra(Intent.EXTRA_TEXT, json)
                }
                context.startActivity(Intent.createChooser(share, "Export sync JSON"))
            }
        }) {
            Text("Export cowlsly_website_settings_sync.json")
        }
        OutlinedTextField(
            value = importJson,
            onValueChange = { importJson = it },
            label = { Text("Paste sync JSON from website") },
            modifier = Modifier.fillMaxWidth(),
        )
        TextButton(
            onClick = { onImport(importJson) { ok -> importResult = if (ok) "Import OK" else "Import failed" } },
            enabled = importJson.isNotBlank(),
        ) {
            Text("Import & sync")
        }
        importResult?.let { Text(it, style = MaterialTheme.typography.labelSmall) }
    }
}

@Composable
private fun PinDialog(
    entry: SettingsEntry?,
    lockRequired: Boolean,
    pinInput: String,
    pinError: Boolean,
    onPinChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onSubmit: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = if (lockRequired) ({}) else onDismiss,
        title = { Text(if (lockRequired) "App locked" else "Enter PIN") },
        text = {
            Column {
                Text(
                    if (lockRequired) "Enter PIN to return to Simple Settings"
                    else "Required for: ${entry?.title ?: "protected setting"}",
                )
                OutlinedTextField(
                    value = pinInput,
                    onValueChange = onPinChange,
                    label = { Text("PIN") },
                    isError = pinError,
                    singleLine = true,
                )
                if (pinError) {
                    Text("Incorrect PIN", color = MaterialTheme.colorScheme.error)
                }
            }
        },
        confirmButton = { TextButton(onClick = onSubmit) { Text("OK") } },
        dismissButton = {
            if (!lockRequired) {
                TextButton(onClick = onDismiss) { Text("Cancel") }
            }
        },
    )
}

private fun launchSystemIntent(
    context: android.content.Context,
    entry: SettingsEntry,
    soundEffects: SoundEffects,
) {
    // Developer gate may only set the action; still open Developer options.
    val target = if (entry.intentAction == null && entry.panelType == SettingsPanelType.DEVELOPER_GATE) {
        entry.copy(intentAction = Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS)
    } else {
        entry
    }
    if (target.intentAction == null && target.componentPackage == null) {
        soundEffects.playDenied()
        return
    }
    val ok = SystemIntentLauncher.launch(context, target)
    if (ok) soundEffects.playPress() else soundEffects.playDenied()
}