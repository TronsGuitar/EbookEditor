package com.tronsguitar.ebookeditor.data.repository

import com.tronsguitar.ebookeditor.data.local.database.dao.ChapterDao
import com.tronsguitar.ebookeditor.data.local.database.dao.SectionDao
import com.tronsguitar.ebookeditor.data.local.database.entity.ChapterEntity
import com.tronsguitar.ebookeditor.data.local.database.entity.SectionEntity
import com.tronsguitar.ebookeditor.domain.model.Chapter
import com.tronsguitar.ebookeditor.domain.model.Section
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Single source of truth for chapter and section data.
 *
 * Maps between Room entities (data layer) and clean domain models. Business
 * logic such as word-count propagation and default-section bootstrapping lives
 * here, keeping DAOs and ViewModels free of structural concerns.
 */
@Singleton
class ChapterRepository @Inject constructor(
    private val chapterDao: ChapterDao,
    private val sectionDao: SectionDao,
) {

    // ── Chapters ─────────────────────────────────────────────────────────────

    fun getChaptersForProject(projectId: Long): Flow<List<Chapter>> =
        chapterDao.getChaptersByProject(projectId).map { entities ->
            entities.map { it.toDomain() }
        }

    /**
     * Creates a new chapter and bootstraps it with a single default section so
     * that content can be written immediately without extra setup.
     *
     * @return the auto-generated chapter id.
     */
    suspend fun createChapter(projectId: Long, title: String, orderIndex: Int): Long {
        val chapterId = chapterDao.insertChapter(
            ChapterEntity(
                projectId = projectId,
                title = title,
                orderIndex = orderIndex,
            ),
        )
        sectionDao.insertSection(
            SectionEntity(
                chapterId = chapterId,
                title = title,
                orderIndex = 0,
            ),
        )
        return chapterId
    }

    suspend fun updateChapter(chapter: Chapter) =
        chapterDao.updateChapter(chapter.toEntity())

    suspend fun deleteChapter(chapter: Chapter) =
        chapterDao.deleteChapter(chapter.toEntity())

    /**
     * Persists a new [orderIndex] for every chapter in the supplied list using
     * its position in the list as the new index.
     */
    suspend fun reorderChapters(chapters: List<Chapter>) {
        val now = System.currentTimeMillis()
        chapters.forEachIndexed { index, chapter ->
            chapterDao.updateOrderIndex(chapter.id, index, now)
        }
    }

    // ── Sections / content ───────────────────────────────────────────────────

    fun getSectionsForChapter(chapterId: Long): Flow<List<Section>> =
        sectionDao.getSectionsByChapter(chapterId).map { entities ->
            entities.map { it.toDomain() }
        }

    /**
     * Returns the text content of a chapter's first (default) section, or an
     * empty string when the chapter has no sections yet.
     */
    suspend fun getChapterContent(chapterId: Long): String =
        sectionDao.getFirstSectionByChapter(chapterId)?.content.orEmpty()

    /**
     * Persists [content] to the chapter's first section and updates the
     * chapter's cached [wordCount]. If no section exists yet (e.g. after a
     * migration) one is created on the fly.
     */
    suspend fun updateChapterContent(chapterId: Long, content: String, wordCount: Int) {
        val now = System.currentTimeMillis()
        val section = sectionDao.getFirstSectionByChapter(chapterId)
        if (section != null) {
            sectionDao.updateContent(section.id, content, wordCount, now)
        } else {
            sectionDao.insertSection(
                SectionEntity(
                    chapterId = chapterId,
                    title = "Section 1",
                    content = content,
                    wordCount = wordCount,
                    orderIndex = 0,
                ),
            )
        }
        chapterDao.updateWordCount(chapterId, wordCount, now)
    }

    // ── Mapping helpers ───────────────────────────────────────────────────────

    private fun ChapterEntity.toDomain() = Chapter(
        id = id,
        projectId = projectId,
        title = title,
        summary = summary,
        orderIndex = orderIndex,
        wordCount = wordCount,
        status = status,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )

    private fun Chapter.toEntity() = ChapterEntity(
        id = id,
        projectId = projectId,
        title = title,
        summary = summary,
        orderIndex = orderIndex,
        wordCount = wordCount,
        status = status,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )

    private fun SectionEntity.toDomain() = Section(
        id = id,
        chapterId = chapterId,
        title = title,
        content = content,
        orderIndex = orderIndex,
        wordCount = wordCount,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )
}
