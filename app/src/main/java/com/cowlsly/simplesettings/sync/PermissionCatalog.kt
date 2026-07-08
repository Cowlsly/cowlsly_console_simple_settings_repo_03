package com.cowlsly.simplesettings.sync

object PermissionCatalog {
    val all: List<PermissionInstruction> = listOf(
        PermissionInstruction(
            id = "modify_audio",
            title = "Volume control",
            why = "Keeps your safe volume steps in sync with the phone's real media volume.",
            steps = "Granted automatically — no action needed. Thanks for trusting Simple Settings with sound.",
            requiredFor = listOf("volume", "sound_settings"),
        ),
        PermissionInstruction(
            id = "write_settings",
            title = "Modify system settings",
            why = "Lets brightness and a few quick controls write back to Android — not just open a menu.",
            steps = "Tap Allow below → find Simple Settings → turn on \"Allow modify system settings\".",
            requiredFor = listOf("brightness_quick", "display_settings"),
        ),
        PermissionInstruction(
            id = "shizuku",
            title = "Shizuku (optional)",
            why = "Unlocks hidden developer and secure settings that normal apps cannot write.",
            steps = "Install Shizuku → start it → grant permission when Simple Settings asks. Thanks.",
            requiredFor = listOf("shizuku_status", "appops"),
        ),
        PermissionInstruction(
            id = "casmea_read",
            title = "CASMEA data sharing",
            why = "Emergency app reads medical info you enter here — never the other way around.",
            steps = "You choose what to save. CASMEA only reads with your consent via the suite contract.",
            requiredFor = listOf("casmea_entry"),
        ),
        PermissionInstruction(
            id = "account_sync",
            title = "Cowlsly account on this device",
            why = "Adds Cowlsly to Android Accounts so Simple Settings and the website self-sync. Panel order stays on the app; web stays A→Z.",
            steps = "Enter your Cowlsly email → tap Add account → allow sync in Accounts & sync. Thanks, bro.",
            requiredFor = listOf("cowlsly_account"),
        ),
        PermissionInstruction(
            id = "internet",
            title = "Open Cowlsly Console",
            why = "Opens cowlsly.com Console Settings in your browser when you tap the logo button.",
            steps = "Uses your browser — no background tracking. Make an account when you're ready.",
            requiredFor = listOf("cowlsly_console"),
        ),
    )

    fun forEntry(entryId: String): List<PermissionInstruction> =
        all.filter { entryId in it.requiredFor }
}