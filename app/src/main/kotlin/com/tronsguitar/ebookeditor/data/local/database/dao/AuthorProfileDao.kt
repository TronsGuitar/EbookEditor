package com.tronsguitar.ebookeditor.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tronsguitar.ebookeditor.data.local.database.entity.AuthorProfileEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AuthorProfileDao {

    @Query("SELECT * FROM author_profiles ORDER BY updatedAt DESC, id DESC LIMIT 1")
    fun getLatestProfile(): Flow<AuthorProfileEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAuthorProfile(profile: AuthorProfileEntity)
}
