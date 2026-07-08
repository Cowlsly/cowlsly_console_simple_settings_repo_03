package com.cowlsly.simplesettings.ui.components

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.OpenInNew
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.cowlsly.simplesettings.CowlslyConsole
import com.cowlsly.simplesettings.R
import com.cowlsly.simplesettings.audio.SoundEffects
import com.cowlsly.simplesettings.ui.theme.CowlslyBgDeep
import com.cowlsly.simplesettings.ui.theme.CowlslyCyan
import com.cowlsly.simplesettings.ui.theme.CowlslyGoldLight

/**
 * Pinned favourite on page 1 — always opens Cowlsly Console Settings on the web.
 * Polishing pass: website + app share the same console look-and-feel.
 */
@Composable
fun CowlslyConsoleSettingsButton(
    soundEffects: SoundEffects,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    val scale by animateFloatAsState(if (pressed) 0.96f else 1f, label = "consoleBtn")

    GlassPanel(modifier = modifier) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(
                text = stringResource(R.string.cowlsly_console_title),
                style = MaterialTheme.typography.titleMedium,
                color = CowlslyGoldLight,
            )
            Text(
                text = stringResource(R.string.cowlsly_console_welcome),
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.78f),
            )
            Button(
                onClick = {
                    soundEffects.playPress()
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(CowlslyConsole.SETTINGS_URL))
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.startActivity(intent)
                },
                interactionSource = interaction,
                modifier = Modifier
                    .fillMaxWidth()
                    .scale(scale),
                colors = ButtonDefaults.buttonColors(
                    containerColor = CowlslyCyan,
                    contentColor = CowlslyBgDeep,
                ),
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Image(
                        painter = painterResource(R.drawable.cowlsly_logo_small),
                        contentDescription = stringResource(R.string.cowlsly_console_logo),
                        modifier = Modifier.size(32.dp),
                    )
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = stringResource(R.string.cowlsly_console_button),
                            style = MaterialTheme.typography.titleSmall,
                        )
                        Text(
                            text = stringResource(R.string.cowlsly_console_subtitle),
                            style = MaterialTheme.typography.labelSmall,
                        )
                    }
                    Icon(
                        Icons.AutoMirrored.Filled.OpenInNew,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                    )
                }
            }
        }
    }
}