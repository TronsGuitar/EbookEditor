package com.tronsguitar.ebookeditor

import com.tronsguitar.ebookeditor.data.local.database.entity.ChapterEntity
import com.tronsguitar.ebookeditor.data.local.database.entity.ExportJobEntity
import com.tronsguitar.ebookeditor.data.local.database.entity.ImportLogEntity
import com.tronsguitar.ebookeditor.domain.model.AuthorProfile
import com.tronsguitar.ebookeditor.domain.model.ComplianceReport
import com.tronsguitar.ebookeditor.domain.model.Metadata
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class CoreDataModelsTest {

    @Test
    fun `metadata has android local defaults`() {
        val metadata = Metadata(projectId = 1)

        assertEquals("en", metadata.language)
        assertEquals("", metadata.keywords)
        assertNull(metadata.publicationDate)
    }

    @Test
    fun `author profile and compliance report default states are stable`() {
        val profile = AuthorProfile(penName = "Jane Doe")
        val report = ComplianceReport(projectId = 2)

        assertEquals("", profile.bio)
        assertEquals("PENDING", report.status)
        assertEquals(0, report.issueCount)
    }

    @Test
    fun `room entities default to local workflow statuses`() {
        val chapter = ChapterEntity(projectId = 1, title = "Chapter 1")
        val importLog = ImportLogEntity(projectId = null, sourceUri = "content://book", sourceType = "DOCX")
        val exportJob = ExportJobEntity(projectId = 1, format = "EPUB")

        assertEquals("DRAFT", chapter.status)
        assertEquals("SUCCESS", importLog.status)
        assertEquals("QUEUED", exportJob.status)
    }
}
