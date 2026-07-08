package com.cowlsly.simplesettings.account

import android.app.Service
import android.content.Intent
import android.os.IBinder

class CowlslySyncService : Service() {
    private val syncAdapter: CowlslySyncAdapter by lazy {
        CowlslySyncAdapter(applicationContext, true)
    }

    override fun onCreate() {
        super.onCreate()
        synchronized(syncAdapterLock) { syncAdapter }
    }

    override fun onBind(intent: Intent): IBinder = syncAdapter.syncAdapterBinder

    companion object {
        private val syncAdapterLock = Any()
    }
}