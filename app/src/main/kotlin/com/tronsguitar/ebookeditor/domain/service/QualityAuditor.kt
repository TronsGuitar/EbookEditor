package com.tronsguitar.ebookeditor.domain.service

import com.tronsguitar.ebookeditor.domain.model.AiInsightService
import com.tronsguitar.ebookeditor.domain.model.Chapter
import com.tronsguitar.ebookeditor.domain.model.Metadata
import com.tronsguitar.ebookeditor.domain.model.Project
import com.tronsguitar.ebookeditor.domain.model.QualityCheckResult
import com.tronsguitar.ebookeditor.domain.model.QualityIssue
import com.tronsguitar.ebookeditor.domain.model.QualityIssueType

class QualityAuditor(
    private val aiInsightService: AiInsightService? = null,
) {
    fun run(
        project: Project,
        metadata: Metadata?,
        chapters: List<Chapter>,
    ): QualityCheckResult {
        val issues = buildList {
            addAll(checkMetadata(project, metadata))
            addAll(checkHeadingOrder(chapters))
            addAll(checkEmptySections(chapters))
            addAll(checkDuplicateTitles(chapters))
        }
        val aiInsights = aiInsightService?.analyze(project, metadata, chapters).orEmpty()
        return QualityCheckResult(
            issues = issues,
            aiInsights = aiInsights,
        )
    }

    private fun checkMetadata(project: Project, metadata: Metadata?): List<QualityIssue> {
        val issues = mutableListOf<QualityIssue>()
        if (project.title.isBlank() || project.authorName.isBlank()) {
            issues += QualityIssue(
                type = QualityIssueType.METADATA,
                message = "Project title and author name are required.",
            )
        }
        if (!project.hasAiDisclosure) {
            issues += QualityIssue(
                type = QualityIssueType.METADATA,
                message = "AI usage disclosure is required for compliance.",
            )
        }
        if (metadata == null) {
            issues += QualityIssue(
                type = QualityIssueType.METADATA,
                message = "Book metadata is missing.",
            )
        } else if (metadata.language.isBlank()) {
            issues += QualityIssue(
                type = QualityIssueType.METADATA,
                message = "Metadata language is required.",
            )
        }
        return issues
    }

    private fun checkHeadingOrder(chapters: List<Chapter>): List<QualityIssue> {
        val issues = mutableListOf<QualityIssue>()
        val chapterIndexes = chapters.map { it.orderIndex }
        if (chapterIndexes != chapterIndexes.distinct().sorted()) {
            issues += QualityIssue(
                type = QualityIssueType.HEADING_ORDER,
                message = "Chapter heading order is inconsistent.",
            )
        }
        chapters.forEach { chapter ->
            val sectionIndexes = chapter.sections.map { it.orderIndex }
            if (sectionIndexes != sectionIndexes.distinct().sorted()) {
                issues += QualityIssue(
                    type = QualityIssueType.HEADING_ORDER,
                    message = "Section heading order is inconsistent in chapter '${chapter.title}'.",
                )
            }
        }
        return issues
    }

    private fun checkEmptySections(chapters: List<Chapter>): List<QualityIssue> =
        chapters.flatMap { chapter ->
            chapter.sections.filter { section ->
                section.title.isBlank() ||
                    // Section content is stored as a flattened string for the editor,
                    // while paragraphs may be present in semantic import flows.
                    // A section is considered empty only when both are blank.
                    (section.content.isBlank() && section.paragraphs.none { it.text.isNotBlank() })
            }.map { section ->
                QualityIssue(
                    type = QualityIssueType.EMPTY_SECTION,
                    message = "Section '${section.title.ifBlank { "(untitled)" }}' in chapter '${chapter.title}' is empty.",
                )
            }
        }

    private fun checkDuplicateTitles(chapters: List<Chapter>): List<QualityIssue> {
        val issues = mutableListOf<QualityIssue>()
        val duplicateChapterTitles = findDuplicates(chapters.map { it.title })
        if (duplicateChapterTitles.isNotEmpty()) {
            issues += QualityIssue(
                type = QualityIssueType.DUPLICATE_TITLE,
                message = "Duplicate chapter titles: ${duplicateChapterTitles.joinToString()}",
            )
        }

        chapters.forEach { chapter ->
            val duplicateSectionTitles = findDuplicates(chapter.sections.map { it.title })
            if (duplicateSectionTitles.isNotEmpty()) {
                issues += QualityIssue(
                    type = QualityIssueType.DUPLICATE_TITLE,
                    message = "Duplicate section titles in chapter '${chapter.title}': ${duplicateSectionTitles.joinToString()}",
                )
            }
        }

        return issues
    }

    /**
     * Finds duplicate titles after normalizing each value by trimming and lowercasing.
     * This intentionally treats casing/whitespace variants as the same title.
     */
    private fun findDuplicates(titles: List<String>): List<String> =
        titles.asSequence()
            .map { it.trim().lowercase() }
            .filter { it.isNotBlank() }
            .groupingBy { it }
            .eachCount()
            .filterValues { it > 1 }
            .keys
            .toList()
}
