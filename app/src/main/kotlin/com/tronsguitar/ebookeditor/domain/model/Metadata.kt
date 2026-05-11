package com.tronsguitar.ebookeditor.domain.model

data class Metadata(
    val id: Long = 0,
    val projectId: Long,
    val subtitle: String = "",
    val language: String = "en",
    val isbn: String = "",
    val keywords: String = "",
    val description: String = "",
    val publisher: String = "",
    val publicationDate: Long? = null,
    val updatedAt: Long = System.currentTimeMillis(),
)
