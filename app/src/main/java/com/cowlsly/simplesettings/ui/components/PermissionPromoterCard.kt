package com.cowlsly.simplesettings.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.cowlsly.simplesettings.R
import com.cowlsly.simplesettings.sync.PermissionInstruction
import com.cowlsly.simplesettings.ui.theme.CowlslyGoldLight

@Composable
fun PermissionPromoterCard(
    instruction: PermissionInstruction,
    onAllow: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    GlassPanel(modifier = modifier) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = stringResource(R.string.permission_promoter_heading),
                style = MaterialTheme.typography.labelSmall,
                color = Color.White.copy(0.65f),
            )
            Text(instruction.title, style = MaterialTheme.typography.titleMedium, color = CowlslyGoldLight)
            Text(instruction.why, style = MaterialTheme.typography.bodySmall)
            Text(instruction.steps, style = MaterialTheme.typography.bodySmall, color = Color.White.copy(0.8f))
            Button(onClick = onAllow, modifier = Modifier.fillMaxWidth()) {
                Text(stringResource(R.string.permission_promoter_allow))
            }
            TextButton(onClick = onDismiss, modifier = Modifier.fillMaxWidth()) {
                Text(stringResource(R.string.permission_promoter_later))
            }
        }
    }
}