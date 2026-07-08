package com.cowlsly.simplesettings.ui.panels

import android.app.role.RoleManager
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.cowlsly.simplesettings.CowlslyConsole
import com.cowlsly.simplesettings.data.SettingsEntry
import com.cowlsly.simplesettings.data.SettingsRankHint
import com.cowlsly.simplesettings.ui.components.SettingsPanelHeader

@Composable
fun SpotifyDefaultPanel(
    entry: SettingsEntry,
    rankHint: SettingsRankHint,
    onOpened: () -> Unit = {},
) {
    val context = LocalContext.current
    val spotifyInstalled = rememberSpotifyInstalled()
    val roleLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
    ) { }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        SettingsPanelHeader(entry = entry, rankHint = rankHint)
        Text(
            if (spotifyInstalled) "Choose Spotify when Android asks for your default music app."
            else "Install Spotify from Play Store first, then set it as default music.",
            style = MaterialTheme.typography.bodySmall,
        )
        Button(
            onClick = {
                onOpened()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    val roleManager = context.getSystemService(RoleManager::class.java)
                    if (roleManager.isRoleAvailable(MUSIC_ROLE)) {
                        val intent = roleManager.createRequestRoleIntent(MUSIC_ROLE)
                        roleLauncher.launch(intent)
                        return@Button
                    }
                }
                context.startActivity(
                    Intent(Settings.ACTION_MANAGE_DEFAULT_APPS_SETTINGS)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK),
                )
            },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("Set Spotify as default music app")
        }
        if (!spotifyInstalled) {
            Button(
                onClick = {
                    onOpened()
                    val market = Intent(Intent.ACTION_VIEW).apply {
                        data = android.net.Uri.parse("market://details?id=${CowlslyConsole.SPOTIFY_PACKAGE}")
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    }
                    runCatching { context.startActivity(market) }
                },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("Install Spotify")
            }
        }
    }
}

private const val MUSIC_ROLE = "android.app.role.MUSIC"

@Composable
private fun rememberSpotifyInstalled(): Boolean {
    val context = LocalContext.current
    return runCatching {
        context.packageManager.getPackageInfo(CowlslyConsole.SPOTIFY_PACKAGE, 0)
        true
    }.getOrElse {
        false
    }
}