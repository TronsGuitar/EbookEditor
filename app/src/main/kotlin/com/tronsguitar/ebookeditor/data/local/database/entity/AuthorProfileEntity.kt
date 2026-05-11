package com.tronsguitar.ebookeditor.data.local.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "author_profiles")
data class AuthorProfileEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val penName: String,
    val bio: String = "",
    val website: String = "",
    val email: String = "",
    val updatedAt: Long = System.currentTimeMillis(),
)
