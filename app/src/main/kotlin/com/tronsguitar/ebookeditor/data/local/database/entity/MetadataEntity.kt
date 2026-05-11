package com.tronsguitar.ebookeditor.data.local.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "metadata",
    foreignKeys = [
        ForeignKey(
            entity = ProjectEntity::class,
            parentColumns = ["id"],
            childColumns = ["projectId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [Index(value = ["projectId"], unique = true)],
)
data class MetadataEntity(
    @PrimaryKey(autoGenerate = true)
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
