package com.tronsguitar.ebookeditor.ui.screens.settings

import com.tronsguitar.ebookeditor.domain.model.AuthorProfile
import com.tronsguitar.ebookeditor.domain.model.Metadata
import com.tronsguitar.ebookeditor.domain.model.Project
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test

class SettingsViewModelTest {

    @Test
    fun `autopopulate uses project values when metadata is missing`() {
        val project = Project(
            id = 42,
            title = "Whispering Gallery",
            authorName = "Elena Vance",
            genre = "Mystery",
            synopsis = "A lighthouse mystery.",
            hasAiDisclosure = true,
        )

        val uiState = SettingsViewModel.autoPopulateUiState(project, metadata = null)

        assertEquals(project.title, uiState.title)
        assertEquals(project.authorName, uiState.authorName)
        assertEquals(project.genre, uiState.keywords)
        assertEquals(project.synopsis, uiState.description)
        assertEquals(project.authorName, uiState.publisher)
        assertEquals("en", uiState.language)
        assertFalse(uiState.hasUnsavedChanges)
    }

    @Test
    fun `autopopulate preserves explicit metadata fields`() {
        val project = Project(
            id = 10,
            title = "Draft",
            authorName = "Author",
            genre = "Fantasy",
            synopsis = "Draft synopsis",
        )
        val metadata = Metadata(
            id = 7,
            projectId = 10,
            subtitle = "Subtitle",
            language = "fr",
            isbn = "123",
            keywords = "custom keywords",
            description = "Custom description",
            publisher = "Custom publisher",
        )

        val uiState = SettingsViewModel.autoPopulateUiState(project, metadata)

        assertEquals("Subtitle", uiState.subtitle)
        assertEquals("fr", uiState.language)
        assertEquals("123", uiState.isbn)
        assertEquals("custom keywords", uiState.keywords)
        assertEquals("Custom description", uiState.description)
        assertEquals("Custom publisher", uiState.publisher)
    }

    @Test
    fun `autopopulate falls back to author profile for blank metadata defaults`() {
        val project = Project(
            id = 11,
            title = "Draft",
            authorName = "Project Author",
            genre = "Fantasy",
            synopsis = "",
        )
        val profile = AuthorProfile(
            penName = "Pen Name",
            bio = "Profile bio",
        )

        val uiState = SettingsViewModel.autoPopulateUiState(
            project = project,
            metadata = null,
            authorProfile = profile,
        )

        assertEquals("Profile bio", uiState.description)
        assertEquals("Pen Name", uiState.publisher)
    }
}
