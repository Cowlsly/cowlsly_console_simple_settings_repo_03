package com.cowlsly.simplesettings.shizuku

import android.content.pm.PackageManager
import rikka.shizuku.Shizuku

data class ShizukuStatus(
    val installed: Boolean,
    val binderAvailable: Boolean,
    val permissionGranted: Boolean,
    val version: Int,
)

object ShizukuHelper {
    fun status(): ShizukuStatus {
        val installed = try {
            Shizuku.getVersion() >= 0
            true
        } catch (_: Exception) {
            false
        }
        val binder = installed && Shizuku.pingBinder()
        val permission = binder && Shizuku.checkSelfPermission() == PackageManager.PERMISSION_GRANTED
        val version = if (installed) Shizuku.getVersion() else 0
        return ShizukuStatus(installed, binder, permission, version)
    }

    fun requestPermission(requestCode: Int) {
        if (Shizuku.pingBinder() && Shizuku.checkSelfPermission() != PackageManager.PERMISSION_GRANTED) {
            Shizuku.requestPermission(requestCode)
        }
    }
}