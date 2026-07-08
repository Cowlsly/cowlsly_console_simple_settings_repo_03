package com.cowlsly.simplesettings.ui.panels

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.cowlsly.simplesettings.CowlslyConsole
import com.cowlsly.simplesettings.data.SettingsEntry
import com.cowlsly.simplesettings.data.SettingsPanelType
import com.cowlsly.simplesettings.data.SystemIntentLauncher
import com.cowlsly.simplesettings.ui.components.SettingsPanelHeader
import com.cowlsly.simplesettings.ui.theme.CowlslyCyan

@Composable
fun DevOpsToolsPanel(
    entry: SettingsEntry,
    onExportSync: () -> Unit,
) {
    val context = LocalContext.current

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        SettingsPanelHeader(entry = entry)
        Text(
            "Cowlsly Console DevOps — deploy pipelines, sync coherence checks, and developer diagnostics for the suite.",
            style = MaterialTheme.typography.bodySmall,
        )
        Text(
            "App panels float by usage and how long since you last opened them; the website stays A→Z.",
            style = MaterialTheme.typography.labelSmall,
            color = CowlslyCyan,
        )
        Button(
            onClick = {
                context.startActivity(
                    Intent(Intent.ACTION_VIEW, Uri.parse(CowlslyConsole.DEVOPS_URL))
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK),
                )
            },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("Open Cowlsly DevOps console")
        }
        OutlinedButton(
            onClick = {
                context.startActivity(
                    Intent(Intent.ACTION_VIEW, Uri.parse(CowlslyConsole.SETTINGS_URL))
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK),
                )
            },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("Cowlsly Console settings")
        }
        OutlinedButton(
            onClick = onExportSync,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("Export suite sync JSON")
        }
        OutlinedButton(
            onClick = {
                SystemIntentLauncher.launch(
                    context,
                    SettingsEntry(
                        id = "devops_wireless_debug",
                        title = "Wireless debugging",
                        basePriority = 11,
                        panelType = SettingsPanelType.SYSTEM_INTENT,
                        intentAction = "android.settings.WIRELESS_DEBUGGING_SETTINGS",
                        requiresDeveloperAccess = true,
                        fallbackActions = listOf(Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS),
                        componentPackage = "com.android.settings",
                        componentClass = "com.android.settings.Settings\$WirelessDebuggingActivity",
                    ),
                )
            },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("Wireless debugging — tap to open")
        }
        OutlinedButton(
            onClick = { SystemIntentLauncher.launchDeveloperOptions(context) },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("Developer options (ADB) — tap to open")
        }
    }
}