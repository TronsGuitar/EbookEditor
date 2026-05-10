package com.tronsguitar.ebookeditor.domain.model

/**
 * Domain model representing an author's manuscript project.
 *
 * This is the clean, layer-agnostic representation used by the UI and
 * business logic. It is mapped to/from [ProjectEntity] in the data layer.
 */
data class Project(
    val id: Long = 0,
    val title: String,
    val authorName: String,
    val genre: String = "",
    val synopsis: String = "",
    val wordCount: Int = 0,
    val weeklyWordGoal: Int = 0,
    val status: ProjectStatus = ProjectStatus.DRAFT,
    val hasAiDisclosure: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
)

enum class ProjectStatus {
    DRAFT,
    IN_PROGRESS,
    REVIEW,
    PUBLISHED,
}
