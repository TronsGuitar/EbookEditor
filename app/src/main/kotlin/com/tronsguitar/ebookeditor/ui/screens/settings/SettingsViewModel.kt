package com.tronsguitar.ebookeditor.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tronsguitar.ebookeditor.data.repository.MetadataRepository
import com.tronsguitar.ebookeditor.data.repository.ProjectRepository
import com.tronsguitar.ebookeditor.domain.model.Metadata
import com.tronsguitar.ebookeditor.domain.model.Project
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SettingsUiState(
    val projectId: Long? = null,
    val title: String = "",
    val authorName: String = "",
    val genre: String = "",
    val synopsis: String = "",
    val hasAiDisclosure: Boolean = false,
    val subtitle: String = "",
    val language: String = "en",
    val isbn: String = "",
    val keywords: String = "",
    val description: String = "",
    val publisher: String = "",
    val isSaving: Boolean = false,
    val hasUnsavedChanges: Boolean = false,
    val statusMessage: String = "",
    val hasProject: Boolean = false,
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val projectRepository: ProjectRepository,
    private val metadataRepository: MetadataRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    private var currentProject: Project? = null
    private var currentMetadataId: Long = 0L
    private var metadataJob: Job? = null

    init {
        observeCurrentProject()
    }

    private fun observeCurrentProject() {
        viewModelScope.launch {
            projectRepository.getAllProjects().collect { projects ->
                val project = projects.firstOrNull()
                if (project == null) {
                    metadataJob?.cancel()
                    currentProject = null
                    currentMetadataId = 0L
                    _uiState.value = SettingsUiState(
                        statusMessage = "",
                        hasProject = false,
                    )
                    return@collect
                }

                val projectChanged = currentProject?.id != project.id
                currentProject = project

                if (projectChanged) {
                    metadataJob?.cancel()
                    metadataJob = viewModelScope.launch {
                        metadataRepository.getMetadataForProject(project.id).collect { metadata ->
                            currentMetadataId = metadata?.id ?: 0L
                            if (_uiState.value.hasUnsavedChanges) return@collect
                            val populatedState = autoPopulateUiState(project, metadata)
                            _uiState.value = populatedState
                            if (metadata == null) {
                                metadataRepository.upsertMetadata(
                                    Metadata(
                                        projectId = project.id,
                                        subtitle = populatedState.subtitle,
                                        language = populatedState.language,
                                        isbn = populatedState.isbn,
                                        keywords = populatedState.keywords,
                                        description = populatedState.description,
                                        publisher = populatedState.publisher,
                                    ),
                                )
                            }
                        }
                    }
                } else if (!_uiState.value.hasUnsavedChanges) {
                    _uiState.update {
                        it.copy(
                            title = project.title,
                            authorName = project.authorName,
                            genre = project.genre,
                            synopsis = project.synopsis,
                            hasAiDisclosure = project.hasAiDisclosure,
                            hasProject = true,
                        )
                    }
                }
            }
        }
    }

    fun onTitleChange(value: String) = updateDraft { it.copy(title = value) }
    fun onAuthorNameChange(value: String) = updateDraft { it.copy(authorName = value) }
    fun onGenreChange(value: String) = updateDraft { it.copy(genre = value) }
    fun onSynopsisChange(value: String) = updateDraft { it.copy(synopsis = value) }
    fun onHasAiDisclosureChange(value: Boolean) = updateDraft { it.copy(hasAiDisclosure = value) }
    fun onSubtitleChange(value: String) = updateDraft { it.copy(subtitle = value) }
    fun onLanguageChange(value: String) = updateDraft { it.copy(language = value) }
    fun onIsbnChange(value: String) = updateDraft { it.copy(isbn = value) }
    fun onKeywordsChange(value: String) = updateDraft { it.copy(keywords = value) }
    fun onDescriptionChange(value: String) = updateDraft { it.copy(description = value) }
    fun onPublisherChange(value: String) = updateDraft { it.copy(publisher = value) }

    fun saveChanges() {
        val project = currentProject ?: return
        val draft = _uiState.value
        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, statusMessage = "") }
            val now = System.currentTimeMillis()

            val updatedProject = project.copy(
                title = draft.title.trim(),
                authorName = draft.authorName.trim(),
                genre = draft.genre.trim(),
                synopsis = draft.synopsis.trim(),
                hasAiDisclosure = draft.hasAiDisclosure,
                updatedAt = now,
            )
            projectRepository.updateProject(updatedProject)

            metadataRepository.upsertMetadata(
                Metadata(
                    id = currentMetadataId,
                    projectId = updatedProject.id,
                    subtitle = draft.subtitle.trim(),
                    language = draft.language.trim().ifBlank { "en" },
                    isbn = draft.isbn.trim(),
                    keywords = draft.keywords.trim(),
                    description = draft.description.trim(),
                    publisher = draft.publisher.trim(),
                    updatedAt = now,
                ),
            )

            currentProject = updatedProject
            _uiState.update {
                it.copy(
                    isSaving = false,
                    hasUnsavedChanges = false,
                    statusMessage = "saved",
                )
            }
        }
    }

    private fun updateDraft(transform: (SettingsUiState) -> SettingsUiState) {
        _uiState.update { transform(it).copy(hasUnsavedChanges = true, statusMessage = "") }
    }

    companion object {
        fun autoPopulateUiState(project: Project, metadata: Metadata?): SettingsUiState {
            val effectiveKeywords = metadata?.keywords
                ?.takeIf { it.isNotBlank() }
                ?: project.genre
            val effectiveDescription = metadata?.description
                ?.takeIf { it.isNotBlank() }
                ?: project.synopsis
            val effectivePublisher = metadata?.publisher
                ?.takeIf { it.isNotBlank() }
                ?: project.authorName

            return SettingsUiState(
                projectId = project.id,
                title = project.title,
                authorName = project.authorName,
                genre = project.genre,
                synopsis = project.synopsis,
                hasAiDisclosure = project.hasAiDisclosure,
                subtitle = metadata?.subtitle.orEmpty(),
                language = metadata?.language ?: "en",
                isbn = metadata?.isbn.orEmpty(),
                keywords = effectiveKeywords,
                description = effectiveDescription,
                publisher = effectivePublisher,
                hasProject = true,
            )
        }
    }
}
