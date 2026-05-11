package com.tronsguitar.ebookeditor.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.tronsguitar.ebookeditor.ui.screens.dashboard.DashboardScreen
import com.tronsguitar.ebookeditor.ui.screens.editor.EditorScreen
import com.tronsguitar.ebookeditor.ui.screens.export.ExportScreen
import com.tronsguitar.ebookeditor.ui.screens.importmanuscript.ImportScreen
import com.tronsguitar.ebookeditor.ui.screens.settings.SettingsScreen

/**
 * Root navigation graph for The Digital Study.
 *
 * The app starts on [DashboardScreen] and navigates between the five primary
 * sections: Dashboard → Editor, Import, Export, and Settings.
 */
@Composable
fun AppNavigation() {
    val navController = rememberNavController()

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
        ) { backStackEntry ->
            val projectId = backStackEntry.arguments?.getLong("projectId") ?: -1L
            EditorScreen(
                projectId = projectId,
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
