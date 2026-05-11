package com.tronsguitar.ebookeditor.data.repository

import com.tronsguitar.ebookeditor.data.local.database.dao.ProjectDao
import com.tronsguitar.ebookeditor.data.local.database.entity.ProjectEntity
import com.tronsguitar.ebookeditor.domain.model.Project
import com.tronsguitar.ebookeditor.domain.model.ProjectStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Single source of truth for manuscript project data.
 *
 * Maps between the Room [ProjectEntity] (data layer) and the clean [Project]
 * domain model. Business logic (word-count progress, status transitions) lives
 * here rather than in the DAO or ViewModel.
 */
@Singleton
class ProjectRepository @Inject constructor(
    private val projectDao: ProjectDao,
) {

    fun getAllProjects(): Flow<List<Project>> =
        projectDao.getAllProjects().map { entities -> entities.map { it.toDomain() } }

    fun getProjectById(id: Long): Flow<Project?> =
        projectDao.getProjectById(id).map { it?.toDomain() }

    suspend fun createProject(project: Project): Long =
        projectDao.insertProject(project.toEntity())

    suspend fun updateProject(project: Project) =
        projectDao.updateProject(project.toEntity())

    suspend fun deleteProject(project: Project) =
        projectDao.deleteProject(project.toEntity())

    // ── Mapping helpers ───────────────────────────────────────────────────────

    private fun ProjectEntity.toDomain() = Project(
        id              = id,
        title           = title,
        authorName      = authorName,
        genre           = genre,
        synopsis        = synopsis,
        wordCount       = wordCount,
        weeklyWordGoal  = weeklyWordGoal,
        status          = runCatching { ProjectStatus.valueOf(status) }.getOrDefault(ProjectStatus.DRAFT),
        hasAiDisclosure = hasAiDisclosure,
        createdAt       = createdAt,
        updatedAt       = updatedAt,
    )

    private fun Project.toEntity() = ProjectEntity(
        id              = id,
        title           = title,
        authorName      = authorName,
        genre           = genre,
        synopsis        = synopsis,
        wordCount       = wordCount,
        weeklyWordGoal  = weeklyWordGoal,
        status          = status.name,
        hasAiDisclosure = hasAiDisclosure,
        createdAt       = createdAt,
        updatedAt       = updatedAt,
    )
}
