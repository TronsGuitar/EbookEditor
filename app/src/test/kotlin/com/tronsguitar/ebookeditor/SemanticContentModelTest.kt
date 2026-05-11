package com.tronsguitar.ebookeditor

import com.tronsguitar.ebookeditor.domain.model.Chapter
import com.tronsguitar.ebookeditor.domain.model.ManualFormatting
import com.tronsguitar.ebookeditor.domain.model.Paragraph
import com.tronsguitar.ebookeditor.domain.model.Section
import com.tronsguitar.ebookeditor.domain.model.SemanticTextStyle
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class SemanticContentModelTest {

    @Test
    fun `chapter section paragraph hierarchy is represented in domain model`() {
        val paragraph = Paragraph(sectionId = 11, text = "Opening line")
        val section = Section(
            id = 11,
            chapterId = 7,
            title = "Scene 1",
            paragraphs = listOf(paragraph),
        )
        val chapter = Chapter(
            id = 7,
            projectId = 3,
            title = "Chapter One",
            sections = listOf(section),
        )

        assertEquals(1, chapter.sections.size)
        assertEquals(1, chapter.sections.first().paragraphs.size)
        assertEquals("Opening line", chapter.sections.first().paragraphs.first().text)
    }

    @Test
    fun `semantic style takes precedence over manual formatting style request`() {
        val paragraph = Paragraph(
            sectionId = 42,
            text = "Styled body paragraph",
            semanticStyle = SemanticTextStyle.BODY,
            manualFormatting = ManualFormatting(
                requestedStyle = SemanticTextStyle.CHAPTER_TITLE,
                isBold = true,
            ),
        )

        assertEquals(SemanticTextStyle.BODY, paragraph.resolvedStyle())
        assertTrue(paragraph.manualFormatting.isBold)
    }
}
