package com.tronsguitar.ebookeditor.data.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.tronsguitar.ebookeditor.data.local.database.entity.SectionEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for [SectionEntity].
 *
 * Sections are always ordered by [SectionEntity.orderIndex] within a chapter.
 */
@Dao
interface SectionDao {

    @Query("SELECT * FROM sections WHERE chapterId = :chapterId ORDER BY orderIndex ASC")
    fun getSectionsByChapter(chapterId: Long): Flow<List<SectionEntity>>

    @Query("SELECT * FROM sections WHERE chapterId = :chapterId ORDER BY orderIndex ASC LIMIT 1")
    suspend fun getFirstSectionByChapter(chapterId: Long): SectionEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSection(section: SectionEntity): Long

    @Update
    suspend fun updateSection(section: SectionEntity)

    @Delete
    suspend fun deleteSection(section: SectionEntity)

    @Query(
        "UPDATE sections SET content = :content, wordCount = :wordCount, updatedAt = :updatedAt WHERE id = :id",
    )
    suspend fun updateContent(id: Long, content: String, wordCount: Int, updatedAt: Long)

    @Query("UPDATE sections SET orderIndex = :orderIndex, updatedAt = :updatedAt WHERE id = :id")
    suspend fun updateOrderIndex(id: Long, orderIndex: Int, updatedAt: Long)
}
