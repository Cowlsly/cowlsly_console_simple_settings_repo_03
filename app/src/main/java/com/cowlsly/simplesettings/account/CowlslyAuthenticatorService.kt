package com.cowlsly.simplesettings.account

import android.app.Service
import android.content.Intent
import android.os.IBinder

class CowlslyAuthenticatorService : Service() {
    private lateinit var authenticator: CowlslyAuthenticator

    override fun onCreate() {
        super.onCreate()
        authenticator = CowlslyAuthenticator(this)
    }

    override fun onBind(intent: Intent): IBinder = authenticator.iBinder
}