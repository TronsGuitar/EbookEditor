package com.tronsguitar.ebookeditor.data.local.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "import_logs",
    foreignKeys = [
        ForeignKey(
            entity = ProjectEntity::class,
            parentColumns = ["id"],
            childColumns = ["projectId"],
            onDelete = ForeignKey.SET_NULL,
        ),
    ],
    indices = [Index("projectId")],
)
data class ImportLogEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val projectId: Long?,
    val sourceUri: String,
    val sourceType: String,
    val status: String = "SUCCESS",
    val message: String = "",
    val createdAt: Long = System.currentTimeMillis(),
)
