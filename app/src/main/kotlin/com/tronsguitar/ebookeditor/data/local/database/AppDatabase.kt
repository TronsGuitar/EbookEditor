package com.tronsguitar.ebookeditor.data.local.database

import androidx.room.Database
import androidx.room.migration.Migration
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.tronsguitar.ebookeditor.data.local.database.dao.ChapterDao
import com.tronsguitar.ebookeditor.data.local.database.dao.MetadataDao
import com.tronsguitar.ebookeditor.data.local.database.dao.ProjectDao
import com.tronsguitar.ebookeditor.data.local.database.dao.SectionDao
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
 *
 * Version 2 introduces core local tables for chapter/section structure, metadata,
 * author profile, compliance reports, import logs, and export jobs.
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
    abstract fun chapterDao(): ChapterDao
    abstract fun sectionDao(): SectionDao
    abstract fun metadataDao(): MetadataDao

    companion object {
        const val DATABASE_NAME = "ebook_editor_db"

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS chapters (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        projectId INTEGER NOT NULL,
                        title TEXT NOT NULL,
                        summary TEXT NOT NULL,
                        orderIndex INTEGER NOT NULL,
                        wordCount INTEGER NOT NULL,
                        status TEXT NOT NULL,
                        createdAt INTEGER NOT NULL,
                        updatedAt INTEGER NOT NULL,
                        FOREIGN KEY(projectId) REFERENCES projects(id) ON DELETE CASCADE
                    )
                    """.trimIndent(),
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS index_chapters_projectId ON chapters(projectId)")

                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS sections (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        chapterId INTEGER NOT NULL,
                        title TEXT NOT NULL,
                        content TEXT NOT NULL,
                        orderIndex INTEGER NOT NULL,
                        wordCount INTEGER NOT NULL,
                        createdAt INTEGER NOT NULL,
                        updatedAt INTEGER NOT NULL,
                        FOREIGN KEY(chapterId) REFERENCES chapters(id) ON DELETE CASCADE
                    )
                    """.trimIndent(),
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS index_sections_chapterId ON sections(chapterId)")

                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS metadata (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        projectId INTEGER NOT NULL,
                        subtitle TEXT NOT NULL,
                        language TEXT NOT NULL,
                        isbn TEXT NOT NULL,
                        keywords TEXT NOT NULL,
                        description TEXT NOT NULL,
                        publisher TEXT NOT NULL,
                        publicationDate INTEGER,
                        updatedAt INTEGER NOT NULL,
                        FOREIGN KEY(projectId) REFERENCES projects(id) ON DELETE CASCADE
                    )
                    """.trimIndent(),
                )
                db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS index_metadata_projectId ON metadata(projectId)")

                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS author_profiles (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        penName TEXT NOT NULL,
                        bio TEXT NOT NULL,
                        website TEXT NOT NULL,
                        email TEXT NOT NULL,
                        updatedAt INTEGER NOT NULL
                    )
                    """.trimIndent(),
                )

                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS compliance_reports (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        projectId INTEGER NOT NULL,
                        status TEXT NOT NULL,
                        issueCount INTEGER NOT NULL,
                        warningCount INTEGER NOT NULL,
                        details TEXT NOT NULL,
                        generatedAt INTEGER NOT NULL,
                        FOREIGN KEY(projectId) REFERENCES projects(id) ON DELETE CASCADE
                    )
                    """.trimIndent(),
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS index_compliance_reports_projectId ON compliance_reports(projectId)")

                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS import_logs (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        projectId INTEGER,
                        sourceUri TEXT NOT NULL,
                        sourceType TEXT NOT NULL,
                        status TEXT NOT NULL,
                        message TEXT NOT NULL,
                        createdAt INTEGER NOT NULL,
                        FOREIGN KEY(projectId) REFERENCES projects(id) ON DELETE SET NULL
                    )
                    """.trimIndent(),
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS index_import_logs_projectId ON import_logs(projectId)")

                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS export_jobs (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        projectId INTEGER NOT NULL,
                        format TEXT NOT NULL,
                        status TEXT NOT NULL,
                        outputUri TEXT,
                        errorMessage TEXT,
                        createdAt INTEGER NOT NULL,
                        completedAt INTEGER,
                        FOREIGN KEY(projectId) REFERENCES projects(id) ON DELETE CASCADE
                    )
                    """.trimIndent(),
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS index_export_jobs_projectId ON export_jobs(projectId)")
            }
        }
    }
}
