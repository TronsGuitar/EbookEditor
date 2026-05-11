package com.tronsguitar.ebookeditor.ui.screens.editor

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tronsguitar.ebookeditor.data.repository.ChapterRepository
import com.tronsguitar.ebookeditor.domain.model.Chapter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * UI state snapshot for the Editor screen.
 *
 * @property chapters           Ordered list of chapters for the open project.
 * @property selectedChapterId  Id of the chapter currently open in the editor.
 * @property content            Raw text of the selected chapter's default section.
 * @property wordCount          Word count of [content] (updated in real-time).
 * @property totalWordCount     Sum of word counts across all chapters.
 * @property autoSaveStatus     Current autosave lifecycle state.
 */
data class EditorUiState(
    val chapters: List<Chapter> = emptyList(),
    val selectedChapterId: Long? = null,
    val content: String = "",
    val wordCount: Int = 0,
    val totalWordCount: Int = 0,
    val autoSaveStatus: AutoSaveStatus = AutoSaveStatus.IDLE,
)

/** Lifecycle stages of the background autosave process. */
enum class AutoSaveStatus { IDLE, SAVING, SAVED }

/**
 * ViewModel for the Editor screen.
 *
 * Responsibilities:
 * - Expose the chapter list for the current project as a [StateFlow].
 * - Load and persist chapter content via [ChapterRepository].
 * - Autosave content changes after a short debounce period.
 * - Track per-chapter and total word counts.
 * - Provide chapter CRUD and reorder operations.
 */
@HiltViewModel
class EditorViewModel @Inject constructor(
    private val chapterRepository: ChapterRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val projectId: Long = checkNotNull(savedStateHandle["projectId"]) {
        "EditorViewModel requires a 'projectId' argument in SavedStateHandle"
    }

    private val _uiState = MutableStateFlow(EditorUiState())
    val uiState: StateFlow<EditorUiState> = _uiState.asStateFlow()

    private var autosaveJob: Job? = null

    init {
        observeChapters()
    }

    // ── Chapter list ─────────────────────────────────────────────────────────

    private fun observeChapters() {
        viewModelScope.launch {
            chapterRepository.getChaptersForProject(projectId).collect { chapters ->
                val previousSelectedId = _uiState.value.selectedChapterId
                val selectedId = when {
                    previousSelectedId != null && chapters.any { it.id == previousSelectedId } ->
                        previousSelectedId
                    else -> chapters.firstOrNull()?.id
                }

                // Only reload content when the selected chapter has changed.
                val contentChanged = selectedId != previousSelectedId
                val content = if (contentChanged && selectedId != null) {
                    chapterRepository.getChapterContent(selectedId)
                } else {
                    _uiState.value.content
                }

                _uiState.update { state ->
                    state.copy(
                        chapters = chapters,
                        selectedChapterId = selectedId,
                        content = if (contentChanged) content else state.content,
                        wordCount = if (contentChanged) countWords(content) else state.wordCount,
                        totalWordCount = chapters.sumOf { it.wordCount },
                    )
                }
            }
        }
    }

    // ── Content editing & autosave ────────────────────────────────────────────

    /**
     * Called by the UI whenever the editor text changes.
     *
     * Updates the in-memory content and word count immediately, then schedules
     * a debounced autosave to persist the change to Room.
     */
    fun onContentChange(newContent: String) {
        val wordCount = countWords(newContent)
        _uiState.update { it.copy(content = newContent, wordCount = wordCount) }
        scheduleAutosave(newContent, wordCount)
    }

    private fun scheduleAutosave(content: String, wordCount: Int) {
        autosaveJob?.cancel()
        _uiState.update { it.copy(autoSaveStatus = AutoSaveStatus.SAVING) }
        autosaveJob = viewModelScope.launch {
            delay(AUTOSAVE_DELAY_MS)
            val chapterId = _uiState.value.selectedChapterId ?: return@launch
            chapterRepository.updateChapterContent(chapterId, content, wordCount)
            _uiState.update { it.copy(autoSaveStatus = AutoSaveStatus.SAVED) }
            delay(SAVED_DISPLAY_DURATION_MS)
            _uiState.update { it.copy(autoSaveStatus = AutoSaveStatus.IDLE) }
        }
    }

    // ── Chapter selection ─────────────────────────────────────────────────────

    /**
     * Switches the editor to [chapterId].
     *
     * Any pending autosave for the previously selected chapter is flushed
     * synchronously before loading the new content so no edits are lost.
     */
    fun selectChapter(chapterId: Long) {
        if (chapterId == _uiState.value.selectedChapterId) return
        viewModelScope.launch {
            flushPendingSave()
            val newContent = chapterRepository.getChapterContent(chapterId)
            _uiState.update { state ->
                state.copy(
                    selectedChapterId = chapterId,
                    content = newContent,
                    wordCount = countWords(newContent),
                    autoSaveStatus = AutoSaveStatus.IDLE,
                )
            }
        }
    }

    /** Immediately persists any unsaved content for the currently selected chapter. */
    private suspend fun flushPendingSave() {
        autosaveJob?.cancel()
        autosaveJob = null
        val chapterId = _uiState.value.selectedChapterId ?: return
        val content = _uiState.value.content
        chapterRepository.updateChapterContent(chapterId, content, countWords(content))
        _uiState.update { it.copy(autoSaveStatus = AutoSaveStatus.IDLE) }
    }

    // ── Chapter CRUD ──────────────────────────────────────────────────────────

    /**
     * Adds a new chapter with the given [title] at the end of the chapter list
     * and selects it so the author can start writing immediately.
     */
    fun addChapter(title: String) {
        if (title.isBlank()) return
        viewModelScope.launch {
            val orderIndex = _uiState.value.chapters.maxOfOrNull { it.orderIndex }?.plus(1) ?: 0
            val newId = chapterRepository.createChapter(projectId, title.trim(), orderIndex)
            selectChapter(newId)
        }
    }

    /** Deletes [chapter] from the project. Selects the next available chapter. */
    fun deleteChapter(chapter: Chapter) {
        viewModelScope.launch {
            chapterRepository.deleteChapter(chapter)
            if (chapter.id == _uiState.value.selectedChapterId) {
                autosaveJob?.cancel()
                val next = _uiState.value.chapters
                    .filter { it.id != chapter.id }
                    .firstOrNull()
                if (next != null) {
                    selectChapter(next.id)
                } else {
                    _uiState.update { it.copy(selectedChapterId = null, content = "") }
                }
            }
        }
    }

    /**
     * Moves the chapter at [fromIndex] to [toIndex] and persists the new order.
     */
    fun reorderChapters(fromIndex: Int, toIndex: Int) {
        val chapters = _uiState.value.chapters.toMutableList()
        if (fromIndex !in chapters.indices || toIndex !in chapters.indices) return
        val moved = chapters.removeAt(fromIndex)
        chapters.add(toIndex, moved)
        _uiState.update { it.copy(chapters = chapters) }
        viewModelScope.launch {
            chapterRepository.reorderChapters(chapters)
        }
    }

    // ── Utility ──────────────────────────────────────────────────────────────

    companion object {
        /** Debounce delay before persisting an autosave. */
        const val AUTOSAVE_DELAY_MS = 2_000L

        /** How long the "Saved" indicator remains visible after a successful save. */
        const val SAVED_DISPLAY_DURATION_MS = 2_000L

        /**
         * Counts whitespace-delimited words in [text].
         *
         * Returns 0 for blank input; always ignores leading/trailing whitespace.
         */
        fun countWords(text: String): Int =
            if (text.isBlank()) 0 else text.trim().split(Regex("\\s+")).size
    }
}
