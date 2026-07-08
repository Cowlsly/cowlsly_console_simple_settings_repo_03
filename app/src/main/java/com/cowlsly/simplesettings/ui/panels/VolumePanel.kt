package com.cowlsly.simplesettings.ui.panels

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeOff
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cowlsly.simplesettings.data.SettingsEntry
import com.cowlsly.simplesettings.data.SettingsRankHint
import com.cowlsly.simplesettings.ui.components.SettingsPanelHeader
import com.cowlsly.simplesettings.ui.theme.CowlslyWarning

val VOLUME_STEPS = listOf(0, 25, 50, 75, 90)

@Composable
fun VolumePanel(
    entry: SettingsEntry,
    rankHint: SettingsRankHint,
    stepIndex: Int,
    isMuted: Boolean,
    onStepSelected: (Int) -> Unit,
    onMuteToggled: (Boolean) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            SettingsPanelHeader(entry = entry, rankHint = rankHint, modifier = Modifier.weight(1f))
            IconButton(onClick = { onMuteToggled(!isMuted) }) {
                Icon(
                    imageVector = if (isMuted) {
                        Icons.AutoMirrored.Filled.VolumeOff
                    } else {
                        Icons.AutoMirrored.Filled.VolumeUp
                    },
                    contentDescription = "Mute toggle",
                )
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            VOLUME_STEPS.forEachIndexed { index, pct ->
                FilterChip(
                    selected = !isMuted && stepIndex == index,
                    onClick = {
                        onMuteToggled(false)
                        onStepSelected(index)
                    },
                    label = { Text("$pct%") },
                    enabled = !isMuted || index == 0,
                )
            }
        }
        if (stepIndex >= 3 && !isMuted) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.padding(top = 4.dp),
            ) {
                Icon(Icons.Default.Warning, contentDescription = null, tint = CowlslyWarning)
                Text(
                    "Headphone volume above safe levels can cause permanent hearing damage.",
                    style = MaterialTheme.typography.bodySmall,
                    color = CowlslyWarning,
                )
            }
        }
    }
}