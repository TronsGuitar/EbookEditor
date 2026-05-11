package com.tronsguitar.ebookeditor.data.local.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.tronsguitar.ebookeditor.domain.model.ComplianceReportStatus

@Entity(
    tableName = "compliance_reports",
    foreignKeys = [
        ForeignKey(
            entity = ProjectEntity::class,
            parentColumns = ["id"],
            childColumns = ["projectId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [Index("projectId")],
)
data class ComplianceReportEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val projectId: Long,
    val status: String = ComplianceReportStatus.PENDING,
    val issueCount: Int = 0,
    val warningCount: Int = 0,
    val details: String = "",
    val generatedAt: Long = System.currentTimeMillis(),
)
