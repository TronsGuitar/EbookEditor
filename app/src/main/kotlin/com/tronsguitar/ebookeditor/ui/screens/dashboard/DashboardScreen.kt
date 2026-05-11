package com.tronsguitar.ebookeditor.ui.screens.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.tronsguitar.ebookeditor.R
import com.tronsguitar.ebookeditor.ui.theme.EbookEditorTheme

/**
 * Dashboard Screen – the central hub for recent projects.
 *
 * Displays the list of manuscripts with visual cover previews and progress
 * tracking. Provides access to manuscript import and app settings.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onOpenProject: (Long) -> Unit,
    onImportManuscript: () -> Unit,
    onOpenSettings: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.screen_dashboard_title)) },
                actions = {
                    IconButton(onClick = onOpenSettings) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = stringResource(R.string.cd_settings),
                        )
                    }
                },
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onImportManuscript) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.cd_import_manuscript),
                )
            }
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(text = stringResource(R.string.dashboard_empty_state))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DashboardScreenPreview() {
    EbookEditorTheme {
        DashboardScreen(
            onOpenProject = {},
            onImportManuscript = {},
            onOpenSettings = {},
        )
    }
}
