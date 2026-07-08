package com.cowlsly.simplesettings.data

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri
import com.cowlsly.simplesettings.sync.SuiteSyncStore

/**
 * Read-only sync JSON for Vault, CASMEA, Cowlsly.com — same file as website sync.
 * URI: content://com.cowlsly.simplesettings.sync/document
 */
class SuiteSyncContentProvider : ContentProvider() {
    companion object {
        const val AUTHORITY = "com.cowlsly.simplesettings.sync"
        val DOCUMENT_URI: Uri = Uri.parse("content://$AUTHORITY/document")
        val COLUMNS = arrayOf("json", "revision", "updated_at")
    }

    private val matcher = UriMatcher(UriMatcher.NO_MATCH).apply {
        addURI(AUTHORITY, "document", 1)
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
        val store = SuiteSyncStore(ctx)
        val doc = store.read() ?: return MatrixCursor(COLUMNS).apply {
            addRow(arrayOf("{}", "0", ""))
        }
        return MatrixCursor(COLUMNS).apply {
            addRow(arrayOf(doc.toJson().toString(), doc.revision.toString(), doc.updatedAt))
        }
    }

    override fun getType(uri: Uri): String? = "vnd.android.cursor.item/vnd.cowlsly.suite.sync"
    override fun insert(uri: Uri, values: ContentValues?): Uri? = null
    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int = 0
    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<out String>?): Int = 0
}