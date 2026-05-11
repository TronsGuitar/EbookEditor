package com.tronsguitar.ebookeditor.domain.model

data class ExportJob(
    val id: Long = 0,
    val projectId: Long,
    val format: String,
    val status: String = ExportStatus.QUEUED,
    val outputUri: String? = null,
    val errorMessage: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val completedAt: Long? = null,
)
