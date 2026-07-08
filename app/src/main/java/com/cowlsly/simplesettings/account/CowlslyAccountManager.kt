package com.cowlsly.simplesettings.account

import android.accounts.Account
import android.accounts.AccountManager
import android.content.ContentResolver
import android.content.Context
import android.os.Bundle
import com.cowlsly.simplesettings.CowlslyConsole

object CowlslyAccountManager {
    fun accounts(context: Context): Array<Account> {
        val am = AccountManager.get(context)
        return am.getAccountsByType(CowlslyAccountContract.ACCOUNT_TYPE)
    }

    fun hasAccount(context: Context): Boolean = accounts(context).isNotEmpty()

    fun primaryAccount(context: Context): Account? = accounts(context).firstOrNull()

    fun addAccountIntent(): android.content.Intent =
        AccountManager.newChooseAccountIntent(
            null,
            null,
            arrayOf(CowlslyAccountContract.ACCOUNT_TYPE),
            null,
            null,
            null,
            null,
        )

    fun addAccountExplicit(context: Context, email: String): Boolean {
        if (email.isBlank()) return false
        val am = AccountManager.get(context)
        val account = Account(email.trim(), CowlslyAccountContract.ACCOUNT_TYPE)
        val added = am.addAccountExplicitly(account, "cowlsly_suite", null) ?: false
        if (added) enableAutoSync(context, account)
        return added || accounts(context).any { it.name == email }
    }

    fun enableAutoSync(context: Context, account: Account) {
        ContentResolver.setIsSyncable(account, CowlslyAccountContract.ACCOUNT_AUTHORITY, 1)
        ContentResolver.setSyncAutomatically(account, CowlslyAccountContract.ACCOUNT_AUTHORITY, true)
        ContentResolver.setMasterSyncAutomatically(true)
    }

    fun requestSync(context: Context) {
        val account = primaryAccount(context) ?: return
        val extras = Bundle().apply {
            putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true)
            putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true)
        }
        ContentResolver.requestSync(account, CowlslyAccountContract.ACCOUNT_AUTHORITY, extras)
    }

    fun openSystemAccountsSettings(context: Context) {
        val intent = android.content.Intent(CowlslyConsole.ANDROID_SYNC_SETTINGS).apply {
            addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }
}