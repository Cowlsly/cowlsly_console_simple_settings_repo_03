package com.cowlsly.simplesettings.ui.panels

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.cowlsly.simplesettings.ui.theme.CowlslyCyan
import com.cowlsly.simplesettings.ui.theme.CowlslyGoldLight

data class CreditEntry(
    val name: String,
    val author: String,
    val reason: String,
)

val EXTERNAL_CREDITS = listOf(
    CreditEntry("Shizuku", "Rikka", "Privileged binder API for settings that need elevated access"),
    CreditEntry("Hidden Settings", "Zacharee & community", "Inspiration for surfacing obscure Android settings intents"),
    CreditEntry("Activity Launcher", "Adam Szalkowski", "Pattern for launching hidden system activities safely"),
    CreditEntry("Settings Database Editor", "Speed Software", "Approach to reading secure settings with user consent"),
    CreditEntry("Developer Options unlock", "AOSP / Google", "Build-number tap ritual we document and deep-link"),
    CreditEntry("App Ops", "Android Open Source Project", "Hidden permission toggles we link via system intents"),
    CreditEntry("Material Design", "Google", "Compose components and accessibility patterns"),
)

@Composable
fun CreditsPanel() {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Credits & acknowledgements", style = MaterialTheme.typography.titleMedium, color = CowlslyGoldLight)
        Text(
            "Simple Settings learns from these projects. We surface similar capabilities through intents and Shizuku — fair credit to their makers.",
            style = MaterialTheme.typography.bodySmall,
        )
        EXTERNAL_CREDITS.forEach { credit ->
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text("${credit.name} — ${credit.author}", style = MaterialTheme.typography.titleSmall, color = CowlslyCyan)
                Text(credit.reason, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}