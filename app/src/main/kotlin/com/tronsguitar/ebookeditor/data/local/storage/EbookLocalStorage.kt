package com.tronsguitar.ebookeditor.data.local.storage

import android.content.Context

/**
 * Local key-value storage for manuscript drafts, scoped by project id.
 */
class EbookLocalStorage(context: Context) {
    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun read(projectId: Long): String? = prefs.getString(keyFor(projectId), null)

    fun save(projectId: Long, content: String): Boolean =
        prefs.edit().putString(keyFor(projectId), content).commit()

    fun delete(projectId: Long): Boolean =
        prefs.edit().remove(keyFor(projectId)).commit()

    private fun keyFor(projectId: Long): String = "ebook_content_$projectId"

    private companion object {
        const val PREFS_NAME = "ebook_local_storage"
    }
}
