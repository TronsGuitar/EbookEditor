package com.tronsguitar.ebookeditor.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.LibraryBooks
import androidx.compose.material.icons.outlined.FileUpload
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.tronsguitar.ebookeditor.R
import com.tronsguitar.ebookeditor.ui.screens.dashboard.DashboardScreen
import com.tronsguitar.ebookeditor.ui.screens.editor.EditorScreen
import com.tronsguitar.ebookeditor.ui.screens.export.ExportScreen
import com.tronsguitar.ebookeditor.ui.screens.importmanuscript.ImportScreen
import com.tronsguitar.ebookeditor.ui.screens.settings.SettingsScreen

/** Model for top-level destinations rendered in adaptive navigation UI. */
private data class TopLevelDestination(
    val route: String,
    val label: String,
    val icon: ImageVector,
)

/**
 * Root navigation graph for The Digital Study.
 *
 * The app starts on [DashboardScreen] and navigates between the five primary
 * sections: Dashboard → Editor, Import, Export, and Settings.
 */
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val dashboardLabel = stringResource(R.string.screen_dashboard_title)
    val importLabel = stringResource(R.string.screen_import_title)
    val settingsLabel = stringResource(R.string.screen_settings_title)
    val destinations = remember(dashboardLabel, importLabel, settingsLabel) {
        listOf(
            TopLevelDestination(
                route = Screen.Dashboard.route,
                label = dashboardLabel,
                icon = Icons.AutoMirrored.Outlined.LibraryBooks,
            ),
            TopLevelDestination(
                route = Screen.Import.route,
                label = importLabel,
                icon = Icons.Outlined.FileUpload,
            ),
            TopLevelDestination(
                route = Screen.Settings.route,
                label = settingsLabel,
                icon = Icons.Outlined.Settings,
            ),
        )
    }
    val topLevelRoutes = remember(destinations) { destinations.mapTo(mutableSetOf()) { it.route } }
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination
    val isTopLevel = currentDestination == null ||
        currentDestination.route in topLevelRoutes

    if (isTopLevel) {
        NavigationSuiteScaffold(
            navigationSuiteItems = {
                destinations.forEach { destination ->
                    item(
                        icon = { Icon(destination.icon, contentDescription = destination.label) },
                        label = { Text(destination.label) },
                        selected = currentDestination?.hierarchy?.any { it.route == destination.route } == true,
                        onClick = {
                            navController.navigate(destination.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                    )
                }
            },
        ) {
            AppNavHost(navController = navController)
        }
    } else {
        AppNavHost(navController = navController)
    }
}

@Composable
private fun AppNavHost(
    navController: androidx.navigation.NavHostController,
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Dashboard.route,
    ) {
        composable(Screen.Dashboard.route) {
            DashboardScreen(
                onOpenProject = { projectId ->
                    navController.navigate(Screen.Editor.createRoute(projectId))
                },
                onImportManuscript = {
                    navController.navigate(Screen.Import.route)
                },
                onOpenSettings = {
                    navController.navigate(Screen.Settings.route)
                },
            )
        }

        composable(
            route = Screen.Editor.route,
            arguments = listOf(navArgument("projectId") { type = NavType.LongType }),
        ) {
            EditorScreen(
                onNavigateBack = { navController.popBackStack() },
            )
        }

        composable(Screen.Import.route) {
            ImportScreen(
                onNavigateBack = { navController.popBackStack() },
                onImportComplete = { projectId ->
                    navController.navigate(Screen.Editor.createRoute(projectId)) {
                        popUpTo(Screen.Dashboard.route)
                    }
                },
            )
        }

        composable(
            route = Screen.Export.route,
            arguments = listOf(navArgument("projectId") { type = NavType.LongType }),
        ) { backStackEntry ->
            val projectId = backStackEntry.arguments?.getLong("projectId") ?: -1L
            ExportScreen(
                projectId = projectId,
                onNavigateBack = { navController.popBackStack() },
            )
        }

        composable(Screen.Settings.route) {
            SettingsScreen(
                onNavigateBack = { navController.popBackStack() },
            )
        }
    }
}
