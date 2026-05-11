package com.tronsguitar.ebookeditor

import com.tronsguitar.ebookeditor.domain.model.Project
import com.tronsguitar.ebookeditor.domain.model.ProjectStatus
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Unit tests for domain model logic in the EbookEditor project.
 *
 * These run on the local JVM (no Android device required).
 */
class ProjectDomainTest {

    @Test
    fun `project has default status of DRAFT`() {
        val project = Project(title = "My Novel", authorName = "Jane Doe")
        assertEquals(ProjectStatus.DRAFT, project.status)
    }

    @Test
    fun `project word count defaults to zero`() {
        val project = Project(title = "My Novel", authorName = "Jane Doe")
        assertEquals(0, project.wordCount)
    }

    @Test
    fun `project AI disclosure defaults to false`() {
        val project = Project(title = "My Novel", authorName = "Jane Doe")
        assertEquals(false, project.hasAiDisclosure)
    }
}
