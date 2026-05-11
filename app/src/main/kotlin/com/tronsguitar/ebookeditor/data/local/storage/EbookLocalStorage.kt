package com.tronsguitar.ebookeditor.data.local.storage

import android.content.Context

/**
 * Local key-value storage for manuscript drafts, scoped by project id.
 */
class EbookLocalStorage(context: Context) {
    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun read(projectId: Long): String? = prefs.getString(keyFor(projectId), null)

    fun save(projectId: Long, content: String) {
        prefs.edit().putString(keyFor(projectId), content).apply()
    }

    fun delete(projectId: Long) {
        prefs.edit().remove(keyFor(projectId)).apply()
    }

    private fun keyFor(projectId: Long): String = "ebook_content_$projectId"

    private companion object {
        const val PREFS_NAME = "ebook_local_storage"
    }
}
