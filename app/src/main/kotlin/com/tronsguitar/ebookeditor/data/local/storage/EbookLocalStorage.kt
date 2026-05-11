package com.tronsguitar.ebookeditor.data.local.storage

import android.content.Context
import java.io.File

/**
 * Local key-value storage for manuscript drafts, scoped by project id.
 *
 * In addition to the extracted text content, each project can store its
 * original source file (DOCX or PDF) so that it can be exported as-is.
 */
class EbookLocalStorage(context: Context) {
    private val appContext = context.applicationContext
    private val prefs = appContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun read(projectId: Long): String? = prefs.getString(keyFor(projectId), null)

    fun save(projectId: Long, content: String): Boolean =
        prefs.edit().putString(keyFor(projectId), content).commit()

    fun delete(projectId: Long): Boolean =
        prefs.edit().remove(keyFor(projectId)).commit()

    /**
     * Saves the original source file bytes (DOCX or PDF) to internal storage.
     * The [extension] must be the lowercase file extension without a leading dot
     * (e.g. "docx" or "pdf").
     */
    fun saveOriginal(projectId: Long, bytes: ByteArray, extension: String): Boolean =
        runCatching {
            val dir = File(appContext.filesDir, ORIGINALS_DIR).also { it.mkdirs() }
            File(dir, "$projectId.$extension").writeBytes(bytes)
            prefs.edit().putString(originalExtKey(projectId), extension).commit()
        }.getOrDefault(false)

    /**
     * Returns the stored original source file for [projectId], or null if it
     * was never saved or has been deleted.
     */
    fun loadOriginalFile(projectId: Long): File? {
        val ext = prefs.getString(originalExtKey(projectId), null) ?: return null
        val file = File(File(appContext.filesDir, ORIGINALS_DIR), "$projectId.$ext")
        return if (file.exists()) file else null
    }

    /** Returns the lowercase extension of the stored original file, or null. */
    fun getOriginalExtension(projectId: Long): String? =
        prefs.getString(originalExtKey(projectId), null)

    /**
     * Deletes the stored original source file for [projectId] and its
     * associated metadata.
     */
    fun deleteOriginal(projectId: Long) {
        val ext = prefs.getString(originalExtKey(projectId), null) ?: return
        File(File(appContext.filesDir, ORIGINALS_DIR), "$projectId.$ext").delete()
        prefs.edit().remove(originalExtKey(projectId)).apply()
    }

    private fun keyFor(projectId: Long): String = "ebook_content_$projectId"
    private fun originalExtKey(projectId: Long): String = "original_ext_$projectId"

    private companion object {
        const val PREFS_NAME = "ebook_local_storage"
        const val ORIGINALS_DIR = "originals"
    }
}
