package com.cowlsly.simplesettings.sync

import android.content.Context
import com.cowlsly.simplesettings.CowlslyConsole
import java.io.File
import java.time.Instant
import org.json.JSONObject

class SuiteSyncStore(context: Context) {
    private val appContext = context.applicationContext
    private val syncFile: File =
        File(appContext.filesDir, CowlslyConsole.WEBSITE_SETTINGS_SYNC_FILE)

    fun read(): SuiteSyncDocument? {
        if (!syncFile.exists()) return null
        return runCatching {
            SuiteSyncDocument.fromJson(JSONObject(syncFile.readText()))
        }.getOrNull()
    }

    fun write(document: SuiteSyncDocument) {
        syncFile.writeText(document.toJson().toString(2))
    }

    fun exportUriPath(): String = syncFile.absolutePath

    fun timestamp(): String = Instant.now().toString()
}