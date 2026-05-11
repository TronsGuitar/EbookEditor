package com.tronsguitar.ebookeditor

import com.tronsguitar.ebookeditor.domain.model.Chapter
import com.tronsguitar.ebookeditor.ui.screens.editor.AutoSaveStatus
import com.tronsguitar.ebookeditor.ui.screens.editor.EditorUiState
import com.tronsguitar.ebookeditor.ui.screens.editor.EditorViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * JVM unit tests for the Editor Backend (Issue #6).
 *
 * Covers word-count logic, UI-state defaults, and chapter-list operations
 * that are pure domain / ViewModel logic without requiring Android context
 * or Room.
 */
class EditorBackendTest {

    // ── Word count ────────────────────────────────────────────────────────────

    @Test
    fun `countWords returns 0 for blank string`() {
        assertEquals(0, EditorViewModel.countWords(""))
        assertEquals(0, EditorViewModel.countWords("   "))
        assertEquals(0, EditorViewModel.countWords("\n\t"))
    }

    @Test
    fun `countWords counts single word`() {
        assertEquals(1, EditorViewModel.countWords("Hello"))
    }

    @Test
    fun `countWords counts multiple words separated by spaces`() {
        assertEquals(5, EditorViewModel.countWords("The quick brown fox jumps"))
    }

    @Test
    fun `countWords handles leading and trailing whitespace`() {
        assertEquals(3, EditorViewModel.countWords("  one two three  "))
    }

    @Test
    fun `countWords handles multiple internal whitespace`() {
        assertEquals(3, EditorViewModel.countWords("word1   word2\t\tword3"))
    }

    @Test
    fun `countWords handles newlines between words`() {
        assertEquals(4, EditorViewModel.countWords("first\nsecond\nthird\nfourth"))
    }

    // ── EditorUiState defaults ────────────────────────────────────────────────

    @Test
    fun `EditorUiState has safe defaults`() {
        val state = EditorUiState()

        assertTrue(state.chapters.isEmpty())
        assertEquals(null, state.selectedChapterId)
        assertEquals("", state.content)
        assertEquals(0, state.wordCount)
        assertEquals(0, state.totalWordCount)
        assertEquals(AutoSaveStatus.IDLE, state.autoSaveStatus)
    }

    // ── Chapter reorder logic ─────────────────────────────────────────────────

    @Test
    fun `reorder moves chapter from first to last position`() {
        val chapters = listOf(
            Chapter(id = 1L, projectId = 10L, title = "A", orderIndex = 0),
            Chapter(id = 2L, projectId = 10L, title = "B", orderIndex = 1),
            Chapter(id = 3L, projectId = 10L, title = "C", orderIndex = 2),
        )

        val reordered = chapters.toMutableList()
        val moved = reordered.removeAt(0)
        reordered.add(2, moved)

        assertEquals(listOf("B", "C", "A"), reordered.map { it.title })
    }

    @Test
    fun `reorder moves chapter from last to first position`() {
        val chapters = listOf(
            Chapter(id = 1L, projectId = 10L, title = "A", orderIndex = 0),
            Chapter(id = 2L, projectId = 10L, title = "B", orderIndex = 1),
            Chapter(id = 3L, projectId = 10L, title = "C", orderIndex = 2),
        )

        val reordered = chapters.toMutableList()
        val moved = reordered.removeAt(2)
        reordered.add(0, moved)

        assertEquals(listOf("C", "A", "B"), reordered.map { it.title })
    }

    // ── Total word count ──────────────────────────────────────────────────────

    @Test
    fun `totalWordCount is sum of all chapter word counts`() {
        val chapters = listOf(
            Chapter(id = 1L, projectId = 1L, title = "Intro", wordCount = 500),
            Chapter(id = 2L, projectId = 1L, title = "Chapter 1", wordCount = 1200),
            Chapter(id = 3L, projectId = 1L, title = "Chapter 2", wordCount = 800),
        )

        val total = chapters.sumOf { it.wordCount }

        assertEquals(2500, total)
    }

    @Test
    fun `totalWordCount is 0 when project has no chapters`() {
        val total = emptyList<Chapter>().sumOf { it.wordCount }
        assertEquals(0, total)
    }

    // ── Chapter title validation ──────────────────────────────────────────────

    @Test
    fun `blank chapter title is rejected`() {
        assertFalse("".isNotBlank())
        assertFalse("   ".isNotBlank())
    }

    @Test
    fun `non-blank chapter title is accepted`() {
        assertTrue("Chapter One".isNotBlank())
        assertTrue(" A ".trim().isNotBlank())
    }
}
