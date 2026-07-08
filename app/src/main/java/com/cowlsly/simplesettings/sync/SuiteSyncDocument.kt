package com.cowlsly.simplesettings.sync

import org.json.JSONArray
import org.json.JSONObject

/**
 * Canonical `cowlsly_website_settings_sync.json` — shared by Simple Settings,
 * cowlsly.com, Vault, CASMEA, and Cowlsly.com home.
 *
 * Ordering rule:
 * - **App** uses [appPanelOrder] (usage-based) — never overwritten from web.
 * - **Web** uses [webCatalogAlphabetical] — always A→Z.
 */
data class SuiteSyncDocument(
    val schema: String = SCHEMA,
    val revision: Long = 0,
    val updatedAt: String = "",
    val source: String = "simple_settings_android",
    val panelTint: Float = 0.5f,
    val lockOnBackground: Boolean = true,
    val volumeStepIndex: Int = 2,
    val volumeMuted: Boolean = false,
    val developerAccessGranted: Boolean = false,
    val casmeaConfigured: Boolean = false,
    val systemMirrors: Map<String, String> = emptyMap(),
    val appPanelOrder: List<String> = emptyList(),
    val webCatalogAlphabetical: List<String> = emptyList(),
    val cowlslyAccountLinked: Boolean = false,
) {
    fun toJson(): JSONObject = JSONObject().apply {
        put("schema", schema)
        put("revision", revision)
        put("updatedAt", updatedAt)
        put("source", source)
        put("console", JSONObject().apply {
            put("panelTint", panelTint.toDouble())
            put("lockOnBackground", lockOnBackground)
        })
        put("volume", JSONObject().apply {
            put("stepIndex", volumeStepIndex)
            put("muted", volumeMuted)
        })
        put("security", JSONObject().apply {
            put("developerAccessGranted", developerAccessGranted)
        })
        put("casmea", JSONObject().apply {
            put("configured", casmeaConfigured)
        })
        put("systemMirrors", JSONObject(systemMirrors))
        put("ordering", JSONObject().apply {
            put("appPolicy", "usage_local")
            put("webPolicy", "alphabetical")
            put("appPanelOrder", JSONArray(appPanelOrder))
            put("webCatalogAlphabetical", JSONArray(webCatalogAlphabetical))
        })
        put("account", JSONObject().apply {
            put("cowlslyLinked", cowlslyAccountLinked)
        })
    }

    companion object {
        const val SCHEMA = "cowlsly_website_settings_sync/v1"

        fun fromJson(raw: JSONObject): SuiteSyncDocument {
            val console = raw.optJSONObject("console")
            val volume = raw.optJSONObject("volume")
            val security = raw.optJSONObject("security")
            val casmea = raw.optJSONObject("casmea")
            val mirrors = raw.optJSONObject("systemMirrors")
            val ordering = raw.optJSONObject("ordering")
            val account = raw.optJSONObject("account")
            val mirrorMap = buildMap {
                if (mirrors != null) {
                    mirrors.keys().forEach { key ->
                        put(key, mirrors.optString(key, ""))
                    }
                }
            }
            return SuiteSyncDocument(
                schema = raw.optString("schema", SCHEMA),
                revision = raw.optLong("revision", 0),
                updatedAt = raw.optString("updatedAt", ""),
                source = raw.optString("source", "unknown"),
                panelTint = console?.optDouble("panelTint")?.toFloat() ?: 0.5f,
                lockOnBackground = console?.optBoolean("lockOnBackground") ?: true,
                volumeStepIndex = volume?.optInt("stepIndex") ?: 2,
                volumeMuted = volume?.optBoolean("muted") ?: false,
                developerAccessGranted = security?.optBoolean("developerAccessGranted") ?: false,
                casmeaConfigured = casmea?.optBoolean("configured") ?: false,
                systemMirrors = mirrorMap,
                appPanelOrder = ordering?.optJSONArray("appPanelOrder").toStringList(),
                webCatalogAlphabetical = ordering?.optJSONArray("webCatalogAlphabetical").toStringList(),
                cowlslyAccountLinked = account?.optBoolean("cowlslyLinked") ?: false,
            )
        }

        private fun JSONArray?.toStringList(): List<String> {
            if (this == null) return emptyList()
            return buildList {
                for (i in 0 until length()) {
                    add(optString(i))
                }
            }
        }
    }
}