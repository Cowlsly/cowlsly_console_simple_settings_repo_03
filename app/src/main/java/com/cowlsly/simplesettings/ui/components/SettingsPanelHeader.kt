package com.cowlsly.simplesettings.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.cowlsly.simplesettings.data.SettingsEntry
import com.cowlsly.simplesettings.data.SettingsRankHint
import com.cowlsly.simplesettings.data.SettingsSorter
import com.cowlsly.simplesettings.ui.theme.CowlslyCyan
import com.cowlsly.simplesettings.ui.theme.CowlslyGoldLight
import com.cowlsly.simplesettings.ui.theme.CowlslyWarning

@Composable
fun SettingsPanelHeader(
    entry: SettingsEntry,
    modifier: Modifier = Modifier,
    rankHint: SettingsRankHint = SettingsRankHint.NORMAL,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.Top,
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(
                    settingsIconTintFor(entry.iconKey).copy(alpha = 0.18f),
                ),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = settingsIconFor(entry.iconKey),
                contentDescription = null,
                tint = settingsIconTintFor(entry.iconKey),
                modifier = Modifier.size(24.dp),
            )
        }
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(entry.title, style = MaterialTheme.typography.titleMedium, color = CowlslyGoldLight)
                ZoneChip(basePriority = entry.basePriority)
                RankChip(rankHint = rankHint)
            }
            if (entry.subtitle.isNotBlank()) {
                Text(
                    entry.subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.72f),
                )
            }
        }
    }
}

@Composable
private fun ZoneChip(basePriority: Int) {
    Surface(
        shape = RoundedCornerShape(6.dp),
        color = CowlslyCyan.copy(alpha = 0.12f),
    ) {
        Text(
            text = SettingsSorter.zoneLabel(basePriority),
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
            style = MaterialTheme.typography.labelSmall,
            color = CowlslyCyan.copy(alpha = 0.9f),
        )
    }
}

@Composable
private fun RankChip(rankHint: SettingsRankHint) {
    if (rankHint == SettingsRankHint.NORMAL) return
    val (label, color) = when (rankHint) {
        SettingsRankHint.HOT -> "★ Hot" to CowlslyGoldLight
        SettingsRankHint.WAITING -> "✦ Waiting" to CowlslyWarning
        SettingsRankHint.NORMAL -> return
    }
    Surface(
        shape = RoundedCornerShape(6.dp),
        color = color.copy(alpha = 0.14f),
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
            style = MaterialTheme.typography.labelSmall,
            color = color,
        )
    }
}