package com.tronsguitar.ebookeditor.data.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.tronsguitar.ebookeditor.data.local.database.entity.ChapterEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for [ChapterEntity].
 *
 * Returns chapters ordered by [ChapterEntity.orderIndex] so the UI always
 * displays them in the author-defined sequence.
 */
@Dao
interface ChapterDao {

    @Query("SELECT * FROM chapters WHERE projectId = :projectId ORDER BY orderIndex ASC")
    fun getChaptersByProject(projectId: Long): Flow<List<ChapterEntity>>

    @Query("SELECT * FROM chapters WHERE id = :id")
    suspend fun getChapterById(id: Long): ChapterEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChapter(chapter: ChapterEntity): Long

    @Update
    suspend fun updateChapter(chapter: ChapterEntity)

    @Delete
    suspend fun deleteChapter(chapter: ChapterEntity)

    @Query("UPDATE chapters SET orderIndex = :orderIndex, updatedAt = :updatedAt WHERE id = :id")
    suspend fun updateOrderIndex(id: Long, orderIndex: Int, updatedAt: Long)

    @Query("UPDATE chapters SET wordCount = :wordCount, updatedAt = :updatedAt WHERE id = :id")
    suspend fun updateWordCount(id: Long, wordCount: Int, updatedAt: Long)
}
