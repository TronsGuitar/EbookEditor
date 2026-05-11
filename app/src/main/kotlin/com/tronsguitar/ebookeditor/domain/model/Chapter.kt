package com.tronsguitar.ebookeditor.domain.model

data class Chapter(
    val id: Long = 0,
    val projectId: Long,
    val title: String,
    val summary: String = "",
    val sections: List<Section> = emptyList(),
    val orderIndex: Int = 0,
    val wordCount: Int = 0,
    val status: String = ChapterStatus.DRAFT,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
)
