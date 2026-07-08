package com.cowlsly.simplesettings.account

import android.accounts.AbstractAccountAuthenticator
import android.accounts.Account
import android.accounts.AccountAuthenticatorResponse
import android.accounts.AccountManager
import android.content.Context
import android.os.Bundle

class CowlslyAuthenticator(private val context: Context) : AbstractAccountAuthenticator(context) {
    override fun editProperties(response: AccountAuthenticatorResponse, accountType: String): Bundle? = null

    override fun addAccount(
        response: AccountAuthenticatorResponse,
        accountType: String,
        authTokenType: String?,
        requiredFeatures: Array<out String>?,
        options: Bundle?,
    ): Bundle {
        val email = options?.getString(AccountManager.KEY_ACCOUNT_NAME).orEmpty()
        if (email.isBlank()) {
            return Bundle().apply {
                putString(AccountManager.KEY_ACCOUNT_NAME, "")
                putString(AccountManager.KEY_ACCOUNT_TYPE, CowlslyAccountContract.ACCOUNT_TYPE)
            }
        }
        val account = Account(email, CowlslyAccountContract.ACCOUNT_TYPE)
        val am = AccountManager.get(context)
        val added = am.addAccountExplicitly(account, "cowlsly_suite", null) ?: false
        if (added) {
            CowlslyAccountManager.enableAutoSync(context, account)
        }
        return Bundle().apply {
            putString(AccountManager.KEY_ACCOUNT_NAME, email)
            putString(AccountManager.KEY_ACCOUNT_TYPE, CowlslyAccountContract.ACCOUNT_TYPE)
        }
    }

    override fun confirmCredentials(
        response: AccountAuthenticatorResponse,
        account: Account,
        options: Bundle?,
    ): Bundle? = null

    override fun getAuthToken(
        response: AccountAuthenticatorResponse,
        account: Account,
        authTokenType: String,
        options: Bundle?,
    ): Bundle = Bundle().apply {
        putString(AccountManager.KEY_ACCOUNT_NAME, account.name)
        putString(AccountManager.KEY_ACCOUNT_TYPE, account.type)
        putString(AccountManager.KEY_AUTHTOKEN, "cowlsly_sync_token")
    }

    override fun getAuthTokenLabel(authTokenType: String): String = "Cowlsly sync"

    override fun updateCredentials(
        response: AccountAuthenticatorResponse,
        account: Account,
        authTokenType: String?,
        options: Bundle?,
    ): Bundle? = null

    override fun hasFeatures(
        response: AccountAuthenticatorResponse,
        account: Account,
        features: Array<out String>,
    ): Bundle = Bundle().apply { putBoolean(AccountManager.KEY_BOOLEAN_RESULT, true) }

}