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
import com.cowlsly.simplesettings.ui.theme.GlassPanel
import com.cowlsly.simplesettings.ui.theme.LocalPanelTint
import com.cowlsly.simplesettings.ui.theme.tintedBorderColor
import com.cowlsly.simplesettings.ui.theme.tintedGlassColor

@Composable
fun GlassPanel(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    val tint = LocalPanelTint.current
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = tintedGlassColor(GlassPanel, tint),
        border = BorderStroke(1.dp, tintedBorderColor(tint)),
        content = {
            Column(modifier = Modifier.padding(16.dp), content = content)
        },
    )
}