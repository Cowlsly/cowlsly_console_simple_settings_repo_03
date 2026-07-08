package com.cowlsly.simplesettings.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.cowlsly.simplesettings.sync.SyncControl
import com.cowlsly.simplesettings.sync.SyncState
import com.cowlsly.simplesettings.ui.theme.CowlslyCyan
import com.cowlsly.simplesettings.ui.theme.CowlslyGoldLight

@Composable
fun SyncStatusRow(
    syncState: SyncState?,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (syncState == null) return
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                Icons.Default.Sync,
                contentDescription = null,
                tint = if (syncState.synced) CowlslyCyan else Color.White.copy(0.4f),
            )
            Column {
                Text(
                    text = syncState.displayValue,
                    style = MaterialTheme.typography.labelMedium,
                    color = CowlslyGoldLight,
                )
                Text(
                    text = controlLabel(syncState),
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White.copy(0.55f),
                )
            }
        }
        IconButton(onClick = onRefresh) {
            Icon(Icons.Default.Refresh, contentDescription = "Sync from source")
        }
    }
}

private fun controlLabel(state: SyncState): String {
    val control = when (state.control) {
        SyncControl.FULL -> "Synced · full control"
        SyncControl.READ_ONLY -> "Synced · read from ${state.sourceLabel}"
        SyncControl.LOCAL_EXPORT -> "Saved · exported to suite"
    }
    return control
}