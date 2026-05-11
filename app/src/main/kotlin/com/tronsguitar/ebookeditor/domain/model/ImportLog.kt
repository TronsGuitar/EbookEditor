package com.tronsguitar.ebookeditor.domain.model

data class ImportLog(
    val id: Long = 0,
    val projectId: Long?,
    val sourceUri: String,
    val sourceType: String,
    val status: String = ImportStatus.SUCCESS,
    val message: String = "",
    val createdAt: Long = System.currentTimeMillis(),
)
