package com.tronsguitar.ebookeditor.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.tronsguitar.ebookeditor.data.local.database.dao.ProjectDao
import com.tronsguitar.ebookeditor.data.local.database.entity.ProjectEntity

/**
 * Room database for The Digital Study.
 *
 * Increment [version] and provide a [androidx.room.migration.Migration] whenever
 * the schema changes to avoid destructive migrations in production.
 */
@Database(
    entities = [ProjectEntity::class],
    version = 1,
    exportSchema = true,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun projectDao(): ProjectDao

    companion object {
        const val DATABASE_NAME = "ebook_editor_db"
    }
}
