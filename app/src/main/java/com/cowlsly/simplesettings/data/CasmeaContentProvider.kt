package com.cowlsly.simplesettings.data

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri

/**
 * Read-only contract for CASMEA repo — medical data entered only in Simple Settings.
 * URI: content://com.cowlsly.simplesettings.casmea/profile
 */
class CasmeaContentProvider : ContentProvider() {
    companion object {
        const val AUTHORITY = "com.cowlsly.simplesettings.casmea"
        val PROFILE_URI: Uri = Uri.parse("content://$AUTHORITY/profile")

        val COLUMNS = arrayOf(
            "full_name",
            "blood_type",
            "allergies",
            "medications",
            "emergency_contact",
            "conditions",
        )
    }

    private val matcher = UriMatcher(UriMatcher.NO_MATCH).apply {
        addURI(AUTHORITY, "profile", 1)
    }

    override fun onCreate(): Boolean = true

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?,
    ): Cursor? {
        if (matcher.match(uri) != 1) return null
        val ctx = context?.applicationContext ?: return null
        val secure = SecurePrefs(ctx)
        return MatrixCursor(COLUMNS).apply {
            addRow(
                arrayOf(
                    secure.getCasmeaField("name"),
                    secure.getCasmeaField("blood"),
                    secure.getCasmeaField("allergies"),
                    secure.getCasmeaField("meds"),
                    secure.getCasmeaField("contact"),
                    secure.getCasmeaField("conditions"),
                ),
            )
        }
    }

    override fun getType(uri: Uri): String? = "vnd.android.cursor.item/vnd.cowlsly.casmea.profile"
    override fun insert(uri: Uri, values: ContentValues?): Uri? = null
    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int = 0
    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<out String>?): Int = 0
}