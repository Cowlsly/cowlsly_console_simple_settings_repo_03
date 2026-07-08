package com.cowlsly.simplesettings.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cowlsly.simplesettings.data.SettingsRankHint
import com.cowlsly.simplesettings.ui.theme.CowlslyGoldLight
import com.cowlsly.simplesettings.ui.theme.CowlslyWarning
import com.cowlsly.simplesettings.ui.theme.GlassPanel
import com.cowlsly.simplesettings.ui.theme.LocalPanelTint
import com.cowlsly.simplesettings.ui.theme.tintedBorderColor
import com.cowlsly.simplesettings.ui.theme.tintedGlassColor

@Composable
fun GlassPanel(
    modifier: Modifier = Modifier,
    rankHint: SettingsRankHint = SettingsRankHint.NORMAL,
    content: @Composable ColumnScope.() -> Unit,
) {
    val tint = LocalPanelTint.current
    val borderColor = when (rankHint) {
        SettingsRankHint.HOT -> CowlslyGoldLight.copy(alpha = 0.55f)
        SettingsRankHint.WAITING -> CowlslyWarning.copy(alpha = 0.45f)
        SettingsRankHint.NORMAL -> tintedBorderColor(tint)
    }
    val borderWidth = if (rankHint == SettingsRankHint.NORMAL) 1.dp else 1.5.dp
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = tintedGlassColor(GlassPanel, tint),
        border = BorderStroke(borderWidth, borderColor),
        content = {
            Column(modifier = Modifier.padding(16.dp), content = content)
        },
    )
}