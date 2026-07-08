package com.cowlsly.simplesettings.data

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class SecurePrefs(context: Context) {
    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val prefs = EncryptedSharedPreferences.create(
        context,
        "simple_settings_secure",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM,
    )

    fun getPin(): String? = prefs.getString(KEY_PIN, null)
    fun setPin(pin: String) { prefs.edit().putString(KEY_PIN, pin).apply() }
    fun hasPin(): Boolean = getPin() != null

    fun getCasmeaField(key: String): String = prefs.getString("casmea_$key", "") ?: ""
    fun setCasmeaFields(fields: Map<String, String>) {
        prefs.edit().apply {
            fields.forEach { (k, v) -> putString("casmea_$k", v) }
        }.apply()
    }

    companion object {
        private const val KEY_PIN = "developer_pin"
    }
}