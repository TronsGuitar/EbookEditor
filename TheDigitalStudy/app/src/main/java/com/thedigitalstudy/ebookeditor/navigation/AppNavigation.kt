package com.thedigitalstudy.ebookeditor.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.FileDownload
import androidx.compose.material.icons.outlined.FileUpload
import androidx.compose.material.icons.automirrored.outlined.LibraryBooks
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.thedigitalstudy.ebookeditor.ui.screens.DashboardScreen
import com.thedigitalstudy.ebookeditor.ui.screens.EditorScreen
import com.thedigitalstudy.ebookeditor.ui.screens.ExportScreen
import com.thedigitalstudy.ebookeditor.ui.screens.ImportScreen
import com.thedigitalstudy.ebookeditor.ui.screens.SettingsScreen

// ── Route model ──────────────────────────────────────────────────────────────

sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
    object Dashboard : Screen("dashboard", "Projects", Icons.AutoMirrored.Outlined.LibraryBooks)
    object Editor    : Screen("editor/{projectId}", "Editor", Icons.Outlined.Edit) {
        fun createRoute(projectId: String) = "editor/$projectId"
    }
    object Import   : Screen("import",   "Import",   Icons.Outlined.FileUpload)
    object Export   : Screen("export",   "Export",   Icons.Outlined.FileDownload)
    object Settings : Screen("settings", "Settings", Icons.Outlined.Settings)
}

private val topLevelScreens = listOf(
    Screen.Dashboard,
    Screen.Import,
    Screen.Export,
    Screen.Settings,
)

// ── Root composable ───────────────────────────────────────────────────────────

/**
 * NavigationSuiteScaffold automatically picks the correct navigation pattern:
 *   • Compact  (phone portrait)  → NavigationBar (bottom)
 *   • Medium   (foldable closed) → NavigationRail (side)
 *   • Expanded (tablet / open)   → NavigationDrawer (permanent)
 */
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination

    val isTopLevel = topLevelScreens.any { it.route == currentDestination?.route }

    if (isTopLevel) {
        NavigationSuiteScaffold(
            navigationSuiteItems = {
                topLevelScreens.forEach { screen ->
                    item(
                        icon     = { Icon(screen.icon, contentDescription = screen.label) },
                        label    = { Text(screen.label) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick  = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState    = true
                            }
                        }
                    )
                }
            }
        ) {
            AppNavHost(navController)
        }
    } else {
        AppNavHost(navController)
    }
}

// ── NavHost ───────────────────────────────────────────────────────────────────

@Composable
private fun AppNavHost(navController: androidx.navigation.NavHostController) {
    NavHost(
        navController  = navController,
        startDestination = Screen.Dashboard.route,
    ) {
        composable(Screen.Dashboard.route) {
            DashboardScreen(
                onProjectClick = { projectId ->
                    navController.navigate(Screen.Editor.createRoute(projectId))
                }
            )
        }
        composable(Screen.Editor.route) { entry ->
            val projectId = entry.arguments?.getString("projectId").orEmpty()
            EditorScreen(
                projectId = projectId,
                onBack    = { navController.popBackStack() },
            )
        }
        composable(Screen.Import.route)   { ImportScreen() }
        composable(Screen.Export.route)   { ExportScreen() }
        composable(Screen.Settings.route) { SettingsScreen() }
    }
}
