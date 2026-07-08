package com.cowlsly.simplesettings.ui.panels

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.cowlsly.simplesettings.account.CowlslyAccountManager
import com.cowlsly.simplesettings.data.SettingsEntry
import com.cowlsly.simplesettings.data.SettingsRankHint
import com.cowlsly.simplesettings.ui.components.SettingsPanelHeader
import com.cowlsly.simplesettings.ui.theme.CowlslyCyan

@Composable
fun CowlslyAccountPanel(
    entry: SettingsEntry,
    rankHint: SettingsRankHint,
    onAccountLinked: () -> Unit,
) {
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    val linked = remember { mutableStateOf(CowlslyAccountManager.hasAccount(context)) }
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        SettingsPanelHeader(entry = entry, rankHint = rankHint)
        Text(
            "Add your Cowlsly account to this phone's Accounts. Settings values self-sync with cowlsly.com and suite apps.",
            style = MaterialTheme.typography.bodySmall,
        )
        Text(
            if (linked.value) "Account linked — sync enabled"
            else "No Cowlsly account on this device yet",
            color = CowlslyCyan,
            style = MaterialTheme.typography.labelMedium,
        )
        if (!linked.value) {
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Cowlsly email") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
            )
            Button(
                onClick = {
                    if (CowlslyAccountManager.addAccountExplicit(context, email)) {
                        linked.value = true
                        CowlslyAccountManager.requestSync(context)
                        onAccountLinked()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = email.contains("@"),
            ) {
                Text("Add Cowlsly account & grant sync")
            }
        }
        Button(
            onClick = {
                CowlslyAccountManager.openSystemAccountsSettings(context)
                linked.value = CowlslyAccountManager.hasAccount(context)
            },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("Open Accounts & sync in Android Settings")
        }
        Button(
            onClick = {
                CowlslyAccountManager.requestSync(context)
                onAccountLinked()
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = linked.value,
        ) {
            Text("Sync now")
        }
    }
}