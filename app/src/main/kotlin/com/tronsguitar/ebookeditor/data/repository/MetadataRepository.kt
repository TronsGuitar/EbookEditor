package com.tronsguitar.ebookeditor.data.repository

import com.tronsguitar.ebookeditor.data.local.database.dao.MetadataDao
import com.tronsguitar.ebookeditor.data.local.database.entity.MetadataEntity
import com.tronsguitar.ebookeditor.domain.model.Metadata
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MetadataRepository @Inject constructor(
    private val metadataDao: MetadataDao,
) {
    fun getMetadataForProject(projectId: Long): Flow<Metadata?> =
        metadataDao.getMetadataForProject(projectId).map { it?.toDomain() }

    suspend fun upsertMetadata(metadata: Metadata) {
        metadataDao.upsertMetadata(metadata.toEntity())
    }

    private fun MetadataEntity.toDomain() = Metadata(
        id = id,
        projectId = projectId,
        subtitle = subtitle,
        language = language,
        isbn = isbn,
        keywords = keywords,
        description = description,
        publisher = publisher,
        publicationDate = publicationDate,
        updatedAt = updatedAt,
    )

    private fun Metadata.toEntity() = MetadataEntity(
        id = id,
        projectId = projectId,
        subtitle = subtitle,
        language = language,
        isbn = isbn,
        keywords = keywords,
        description = description,
        publisher = publisher,
        publicationDate = publicationDate,
        updatedAt = updatedAt,
    )
}
