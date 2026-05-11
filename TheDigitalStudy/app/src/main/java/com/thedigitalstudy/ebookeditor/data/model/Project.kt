package com.thedigitalstudy.ebookeditor.data.model

data class Project(
    val id: String,
    val title: String,
    val author: String,
    val genre: String,
    val status: ProjectStatus,
    val wordCount: Int,
    val wordGoal: Int,
    val lastEdited: String,
) {
    val progress: Float get() = if (wordGoal > 0) wordCount.toFloat() / wordGoal else 0f
}

enum class ProjectStatus(val label: String) {
    DRAFT("Draft"),
    IN_PROGRESS("In Progress"),
    REVIEW("Review"),
    PUBLISHED("Published"),
}

/** Sample data — replace with Room DB persistence in production. */
val sampleProjects = listOf(
    Project(
        id          = "1",
        title       = "The Forgotten Archive",
        author      = "Jane Ashford",
        genre       = "Literary Fiction",
        status      = ProjectStatus.IN_PROGRESS,
        wordCount   = 42_500,
        wordGoal    = 80_000,
        lastEdited  = "May 9, 2026",
    ),
    Project(
        id         = "2",
        title      = "Void Between Stars",
        author     = "Jane Ashford",
        genre      = "Science Fiction",
        status     = ProjectStatus.DRAFT,
        wordCount  = 8_200,
        wordGoal   = 90_000,
        lastEdited = "May 7, 2026",
    ),
    Project(
        id         = "3",
        title      = "The Cartographer's Daughter",
        author     = "Jane Ashford",
        genre      = "Historical Fiction",
        status     = ProjectStatus.REVIEW,
        wordCount  = 74_300,
        wordGoal   = 75_000,
        lastEdited = "Apr 28, 2026",
    ),
    Project(
        id         = "4",
        title      = "Midnight Algorithms",
        author     = "Jane Ashford",
        genre      = "Thriller",
        status     = ProjectStatus.PUBLISHED,
        wordCount  = 68_000,
        wordGoal   = 68_000,
        lastEdited = "Mar 15, 2026",
    ),
)
