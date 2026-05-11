package com.tronsguitar.ebookeditor.domain.model

enum class QualityIssueType {
    HEADING_ORDER,
    EMPTY_SECTION,
    DUPLICATE_TITLE,
    METADATA,
}

data class QualityIssue(
    val type: QualityIssueType,
    val message: String,
)

data class AiInsight(
    val title: String,
    val detail: String,
)

data class QualityCheckResult(
    val issues: List<QualityIssue> = emptyList(),
    val aiInsights: List<AiInsight> = emptyList(),
) {
    val isPassing: Boolean
        get() = issues.isEmpty()
}

fun interface AiInsightService {
    fun analyze(
        project: Project,
        metadata: Metadata?,
        chapters: List<Chapter>,
    ): List<AiInsight>
}
