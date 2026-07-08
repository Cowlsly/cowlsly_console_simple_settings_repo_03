package com.cowlsly.simplesettings.ui.panels

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cowlsly.simplesettings.shizuku.ShizukuHelper
import com.cowlsly.simplesettings.shizuku.ShizukuStatus

@Composable
fun ShizukuPanel(onRequestPermission: () -> Unit) {
    var status by remember { mutableStateOf(ShizukuHelper.status()) }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Shizuku", style = MaterialTheme.typography.titleMedium)
        Text(
            "Grants privileged access for hidden settings, secure settings reads, and developer tooling — same role as recommended companion apps.",
            style = MaterialTheme.typography.bodySmall,
        )
        StatusLine("Installed", status.installed)
        StatusLine("Binder alive", status.binderAvailable)
        StatusLine("Permission granted", status.permissionGranted)
        if (status.version > 0) {
            Text("Shizuku v${status.version}", style = MaterialTheme.typography.labelSmall)
        }
        Button(
            onClick = {
                ShizukuHelper.requestPermission(1001)
                status = ShizukuHelper.status()
                onRequestPermission()
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = status.binderAvailable && !status.permissionGranted,
        ) {
            Text("Request Shizuku permission")
        }
        Button(
            onClick = { status = ShizukuHelper.status() },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("Refresh status")
        }
    }
}

@Composable
private fun StatusLine(label: String, ok: Boolean) {
    Text(
        "$label: ${if (ok) "Yes" else "No"}",
        style = MaterialTheme.typography.bodyMedium,
    )
}