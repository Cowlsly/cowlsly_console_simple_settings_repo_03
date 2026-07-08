package com.cowlsly.simplesettings.sync

import android.util.Log
import com.cowlsly.simplesettings.CowlslyConsole
import java.net.HttpURLConnection
import java.net.URL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

data class WebsiteSyncResult(
    val success: Boolean,
    val message: String,
    val remoteDocument: SuiteSyncDocument? = null,
)

class WebsiteSyncClient {
    suspend fun pull(): WebsiteSyncResult = withContext(Dispatchers.IO) {
        runCatching {
            val url = URL(CowlslyConsole.SYNC_PULL_URL)
            val conn = (url.openConnection() as HttpURLConnection).apply {
                requestMethod = "GET"
                connectTimeout = 8_000
                readTimeout = 8_000
                setRequestProperty("Accept", "application/json")
            }
            val code = conn.responseCode
            if (code !in 200..299) {
                return@withContext WebsiteSyncResult(false, "Website returned HTTP $code")
            }
            val body = conn.inputStream.bufferedReader().readText()
            val doc = SuiteSyncDocument.fromJson(JSONObject(body))
            WebsiteSyncResult(true, "Pulled revision ${doc.revision}", doc)
        }.getOrElse { e ->
            Log.w(TAG, "Pull failed", e)
            WebsiteSyncResult(false, e.message ?: "Pull failed")
        }
    }

    suspend fun push(document: SuiteSyncDocument): WebsiteSyncResult = withContext(Dispatchers.IO) {
        runCatching {
            val url = URL(CowlslyConsole.SYNC_PUSH_URL)
            val conn = (url.openConnection() as HttpURLConnection).apply {
                requestMethod = "POST"
                connectTimeout = 8_000
                readTimeout = 8_000
                doOutput = true
                setRequestProperty("Content-Type", "application/json")
                setRequestProperty("Accept", "application/json")
            }
            conn.outputStream.bufferedWriter().use { it.write(document.toJson().toString()) }
            val code = conn.responseCode
            if (code !in 200..299) {
                return@withContext WebsiteSyncResult(false, "Website push HTTP $code")
            }
            WebsiteSyncResult(true, "Pushed revision ${document.revision}")
        }.getOrElse { e ->
            Log.w(TAG, "Push failed", e)
            WebsiteSyncResult(false, e.message ?: "Push failed")
        }
    }

    companion object {
        private const val TAG = "WebsiteSyncClient"
    }
}