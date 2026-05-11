package com.tronsguitar.ebookeditor.domain.model

data class AuthorProfile(
    val id: Long = 0,
    val penName: String,
    val bio: String = "",
    val website: String = "",
    val email: String = "",
    val updatedAt: Long = System.currentTimeMillis(),
)
