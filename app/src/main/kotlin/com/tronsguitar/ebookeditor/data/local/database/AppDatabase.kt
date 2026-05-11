package com.tronsguitar.ebookeditor.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.tronsguitar.ebookeditor.data.local.database.dao.ProjectDao
import com.tronsguitar.ebookeditor.data.local.database.entity.AuthorProfileEntity
import com.tronsguitar.ebookeditor.data.local.database.entity.ChapterEntity
import com.tronsguitar.ebookeditor.data.local.database.entity.ComplianceReportEntity
import com.tronsguitar.ebookeditor.data.local.database.entity.ExportJobEntity
import com.tronsguitar.ebookeditor.data.local.database.entity.ImportLogEntity
import com.tronsguitar.ebookeditor.data.local.database.entity.MetadataEntity
import com.tronsguitar.ebookeditor.data.local.database.entity.ProjectEntity
import com.tronsguitar.ebookeditor.data.local.database.entity.SectionEntity

/**
 * Room database for The Digital Study.
 *
 * Increment [version] and provide a [androidx.room.migration.Migration] whenever
 * the schema changes to avoid destructive migrations in production.
 */
@Database(
    entities = [
        ProjectEntity::class,
        ChapterEntity::class,
        SectionEntity::class,
        MetadataEntity::class,
        AuthorProfileEntity::class,
        ComplianceReportEntity::class,
        ImportLogEntity::class,
        ExportJobEntity::class,
    ],
    version = 2,
    exportSchema = true,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun projectDao(): ProjectDao

    companion object {
        const val DATABASE_NAME = "ebook_editor_db"
    }
}
