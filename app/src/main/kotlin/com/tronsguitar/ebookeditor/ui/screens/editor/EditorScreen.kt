package com.tronsguitar.ebookeditor.ui.screens.editor

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ExperimentalMaterial3Api
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
 * Editor Screen – the distraction-free Focus Mode editor.
 *
 * Prioritises semantic styles over manual formatting. The text container is
 * capped at 800dp regardless of screen width to maintain the optimal 60-80
 * character line length for professional editing (see Design System spec).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditorScreen(
    projectId: Long,
    onNavigateBack: () -> Unit,
    onExport: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.screen_editor_title)) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.cd_navigate_back),
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onExport) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = stringResource(R.string.cd_export),
                        )
                    }
                },
            )
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center,
        ) {
            Text(text = stringResource(R.string.editor_placeholder, projectId))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun EditorScreenPreview() {
    EbookEditorTheme {
        EditorScreen(
            projectId = 1L,
            onNavigateBack = {},
            onExport = {},
        )
    }
}
