package com.tronsguitar.ebookeditor.data.repository

import com.tronsguitar.ebookeditor.data.local.database.dao.AuthorProfileDao
import com.tronsguitar.ebookeditor.data.local.database.entity.AuthorProfileEntity
import com.tronsguitar.ebookeditor.domain.model.AuthorProfile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthorProfileRepository @Inject constructor(
    private val authorProfileDao: AuthorProfileDao,
) {
    fun getLatestProfile(): Flow<AuthorProfile?> =
        authorProfileDao.getLatestProfile().map { it?.toDomain() }

    suspend fun upsertAuthorProfile(profile: AuthorProfile) {
        authorProfileDao.upsertAuthorProfile(profile.toEntity())
    }

    private fun AuthorProfileEntity.toDomain() = AuthorProfile(
        id = id,
        penName = penName,
        bio = bio,
        website = website,
        email = email,
        updatedAt = updatedAt,
    )

    private fun AuthorProfile.toEntity() = AuthorProfileEntity(
        id = id,
        penName = penName,
        bio = bio,
        website = website,
        email = email,
        updatedAt = updatedAt,
    )
}
