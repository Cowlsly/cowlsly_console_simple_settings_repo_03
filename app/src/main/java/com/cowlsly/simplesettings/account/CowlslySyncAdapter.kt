package com.cowlsly.simplesettings.account

import android.accounts.Account
import android.content.AbstractThreadedSyncAdapter
import android.content.ContentProviderClient
import android.content.Context
import android.content.SyncResult
import android.os.Bundle
import com.cowlsly.simplesettings.SimpleSettingsApplication
import kotlinx.coroutines.runBlocking

class CowlslySyncAdapter(context: Context, autoInitialize: Boolean) :
    AbstractThreadedSyncAdapter(context, autoInitialize) {

    override fun onPerformSync(
        account: Account,
        extras: Bundle,
        authority: String,
        provider: ContentProviderClient,
        syncResult: SyncResult,
    ) {
        val app = context.applicationContext as? SimpleSettingsApplication ?: return
        runBlocking {
            runCatching {
                app.settingsRepository.syncAllCoherent()
            }.onFailure {
                syncResult.stats.numIoExceptions++
            }
        }
    }
}