package com.tronsguitar.ebookeditor.domain.model

data class ComplianceReport(
    val id: Long = 0,
    val projectId: Long,
    val status: String = ComplianceReportStatus.PENDING,
    val issueCount: Int = 0,
    val warningCount: Int = 0,
    val details: String = "",
    val generatedAt: Long = System.currentTimeMillis(),
)
