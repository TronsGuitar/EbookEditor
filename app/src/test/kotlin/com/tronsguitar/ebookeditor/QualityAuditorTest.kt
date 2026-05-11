package com.tronsguitar.ebookeditor

import com.tronsguitar.ebookeditor.domain.model.AiInsight
import com.tronsguitar.ebookeditor.domain.model.Chapter
import com.tronsguitar.ebookeditor.domain.model.Metadata
import com.tronsguitar.ebookeditor.domain.model.Project
import com.tronsguitar.ebookeditor.domain.model.QualityIssueType
import com.tronsguitar.ebookeditor.domain.model.Section
import com.tronsguitar.ebookeditor.domain.service.QualityAuditor
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class QualityAuditorTest {

    @Test
    fun `run reports metadata empty section and duplicate title issues`() {
        val project = Project(
            id = 1,
            title = "Novel",
            authorName = "Author",
            hasAiDisclosure = false,
        )
        val chapters = listOf(
            Chapter(
                id = 1,
                projectId = 1,
                title = "Chapter 1",
                orderIndex = 0,
                sections = listOf(
                    Section(chapterId = 1, title = "", orderIndex = 0),
                    Section(chapterId = 1, title = "Scene A", orderIndex = 1, content = "Text"),
                    Section(chapterId = 1, title = "Scene A", orderIndex = 2, content = "More"),
                ),
            ),
        )

        val result = QualityAuditor().run(project, metadata = null, chapters = chapters)

        assertFalse(result.isPassing)
        assertTrue(result.issues.any { it.type == QualityIssueType.METADATA })
        assertTrue(result.issues.any { it.type == QualityIssueType.EMPTY_SECTION })
        assertTrue(result.issues.any { it.type == QualityIssueType.DUPLICATE_TITLE })
    }

    @Test
    fun `run reports heading order issues for duplicate indexes`() {
        val project = Project(
            id = 7,
            title = "Novel",
            authorName = "Author",
            hasAiDisclosure = true,
        )
        val metadata = Metadata(projectId = 7)
        val chapters = listOf(
            Chapter(
                id = 1,
                projectId = 7,
                title = "A",
                orderIndex = 0,
                sections = listOf(
                    Section(chapterId = 1, title = "One", orderIndex = 0, content = "text"),
                    Section(chapterId = 1, title = "Two", orderIndex = 0, content = "text"),
                ),
            ),
            Chapter(
                id = 2,
                projectId = 7,
                title = "B",
                orderIndex = 0,
                sections = listOf(Section(chapterId = 2, title = "One", orderIndex = 0, content = "text")),
            ),
        )

        val result = QualityAuditor().run(project, metadata, chapters)

        assertTrue(result.issues.any { it.type == QualityIssueType.HEADING_ORDER })
    }

    @Test
    fun `run includes optional AI insights when service is provided`() {
        val project = Project(
            id = 8,
            title = "Novel",
            authorName = "Author",
            hasAiDisclosure = true,
        )
        val metadata = Metadata(projectId = 8)
        val chapters = listOf(
            Chapter(
                id = 1,
                projectId = 8,
                title = "Chapter",
                orderIndex = 0,
                sections = listOf(Section(chapterId = 1, title = "Scene", orderIndex = 0, content = "text")),
            ),
        )
        val expectedInsight = AiInsight(
            title = "Character Arc",
            detail = "The protagonist motivation shifts abruptly in chapter 2.",
        )

        val auditor = QualityAuditor { _, _, _ -> listOf(expectedInsight) }
        val result = auditor.run(project, metadata, chapters)

        assertEquals(listOf(expectedInsight), result.aiInsights)
    }
}
