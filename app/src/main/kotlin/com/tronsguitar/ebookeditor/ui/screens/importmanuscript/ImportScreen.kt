package com.tronsguitar.ebookeditor.ui.screens.importmanuscript

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
 * Import Screen – DOCX and PDF ingestion with automated semantic analysis.
 *
 * Guides the author through file selection, then shows real-time log output as
 * the parsing engine extracts semantic structure (H1, H2, Body) and applies
 * tonal layering to produce reflowable text.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImportScreen(
    onNavigateBack: () -> Unit,
    onImportComplete: (projectId: Long) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.screen_import_title)) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.cd_navigate_back),
                        )
                    }
                },
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(text = stringResource(R.string.import_placeholder))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ImportScreenPreview() {
    EbookEditorTheme {
        ImportScreen(
            onNavigateBack = {},
            onImportComplete = {},
        )
    }
}
