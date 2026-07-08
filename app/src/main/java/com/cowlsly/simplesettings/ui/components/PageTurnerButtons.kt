package com.cowlsly.simplesettings.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import com.cowlsly.simplesettings.audio.SoundEffects
import com.cowlsly.simplesettings.ui.theme.CowlslyBgDeep
import com.cowlsly.simplesettings.ui.theme.CowlslyGoldMid

@Composable
fun PageTurnerButtons(
    currentPage: Int,
    totalPages: Int,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
    soundEffects: SoundEffects,
    modifier: Modifier = Modifier,
) {
    val view = LocalView.current
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        TurnButton(
            label = "Previous",
            icon = Icons.AutoMirrored.Filled.ArrowBack,
            enabled = currentPage > 0,
            onClick = {
                soundEffects.playPageTurn()
                soundEffects.hapticPress(view)
                onPrevious()
            },
        )
        Text(
            text = "Page ${currentPage + 1} of $totalPages",
            style = MaterialTheme.typography.labelLarge,
            color = Color.White.copy(alpha = 0.85f),
        )
        TurnButton(
            label = "Next",
            icon = Icons.AutoMirrored.Filled.ArrowForward,
            enabled = currentPage < totalPages - 1,
            onClick = {
                soundEffects.playPageTurn()
                soundEffects.hapticPress(view)
                onNext()
            },
        )
    }
}

@Composable
private fun TurnButton(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    enabled: Boolean,
    onClick: () -> Unit,
) {
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    val scale by animateFloatAsState(if (pressed && enabled) 0.92f else 1f, label = "btnScale")
    Button(
        onClick = onClick,
        enabled = enabled,
        interactionSource = interaction,
        modifier = Modifier.scale(scale),
        colors = ButtonDefaults.buttonColors(
            containerColor = CowlslyGoldMid,
            contentColor = CowlslyBgDeep,
            disabledContainerColor = Color.White.copy(alpha = 0.12f),
            disabledContentColor = Color.White.copy(alpha = 0.35f),
        ),
        contentPadding = ButtonDefaults.ContentPadding,
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(icon, contentDescription = label, modifier = Modifier.size(18.dp))
            Text(label)
        }
    }
}