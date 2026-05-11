package com.tronsguitar.ebookeditor.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tronsguitar.ebookeditor.data.local.database.entity.MetadataEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MetadataDao {

    @Query("SELECT * FROM metadata WHERE projectId = :projectId LIMIT 1")
    fun getMetadataForProject(projectId: Long): Flow<MetadataEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertMetadata(metadata: MetadataEntity)
}
