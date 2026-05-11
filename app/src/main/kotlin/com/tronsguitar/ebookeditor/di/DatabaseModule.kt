package com.tronsguitar.ebookeditor.di

import android.content.Context
import androidx.room.Room
import com.tronsguitar.ebookeditor.data.local.database.AppDatabase
import com.tronsguitar.ebookeditor.data.local.database.dao.ProjectDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module that provides Room database and DAO instances.
 *
 * All bindings are scoped to [SingletonComponent] so that a single database
 * connection is shared across the application lifecycle.
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            AppDatabase.DATABASE_NAME,
        ).addMigrations(AppDatabase.MIGRATION_1_2)
            .build()

    @Provides
    fun provideProjectDao(database: AppDatabase): ProjectDao = database.projectDao()
}
