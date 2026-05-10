package com.tronsguitar.ebookeditor.data.local.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room entity representing a stored manuscript project.
 *
 * Persists all project metadata required by the dashboard, editor,
 * and D2D export compliance checks.
 */
@Entity(tableName = "projects")
data class ProjectEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val authorName: String,
    val genre: String = "",
    val synopsis: String = "",
    val wordCount: Int = 0,
    val weeklyWordGoal: Int = 0,
    /** One of: DRAFT, IN_PROGRESS, REVIEW, PUBLISHED */
    val status: String = "DRAFT",
    /** Whether the author has acknowledged AI content usage (D2D 2026 compliance). */
    val hasAiDisclosure: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
)
