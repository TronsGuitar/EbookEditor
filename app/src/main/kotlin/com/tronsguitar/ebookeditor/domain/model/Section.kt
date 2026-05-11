package com.tronsguitar.ebookeditor.domain.model

data class Section(
    val id: Long = 0,
    val chapterId: Long,
    val title: String,
    val content: String = "",
    val orderIndex: Int = 0,
    val wordCount: Int = 0,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
)
