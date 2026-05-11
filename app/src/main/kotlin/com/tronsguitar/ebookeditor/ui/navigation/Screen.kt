package com.tronsguitar.ebookeditor.ui.navigation

/**
 * Sealed class representing all top-level navigation destinations in the app.
 *
 * Each entry corresponds to a primary section reachable from the navigation
 * rail / drawer (tablet) or bottom navigation bar (compact).
 */
sealed class Screen(val route: String) {
    data object Dashboard : Screen("dashboard")
    data object Editor    : Screen("editor/{projectId}") {
        fun createRoute(projectId: Long) = "editor/$projectId"
    }
    data object Import    : Screen("import")
    data object Export    : Screen("export/{projectId}") {
        fun createRoute(projectId: Long) = "export/$projectId"
    }
    data object Settings  : Screen("settings")
}
