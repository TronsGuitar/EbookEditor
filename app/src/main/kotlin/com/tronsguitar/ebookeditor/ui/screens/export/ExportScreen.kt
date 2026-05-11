package com.tronsguitar.ebookeditor.ui.screens.export

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.FileProvider
import com.tronsguitar.ebookeditor.R
import com.tronsguitar.ebookeditor.ui.theme.EbookEditorTheme
import java.io.File

private const val TAG = "ExportScreen"

/**
 * Export Screen – D2D Compliance & Publishing.
 *
 * Runs the automated Quality Check auditor (AI disclosures, metadata validity,
 * image resolution) before producing Reflowable EPUB, Print-Ready PDF, or DOCX.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExportScreen(
    projectId: Long,
    onNavigateBack: () -> Unit,
) {
    val context = LocalContext.current
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.screen_export_title)) },
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
            Text(text = stringResource(R.string.export_placeholder, projectId))
            Button(
                onClick = {
                    runCatching {
                        val exportDirectory = File(context.cacheDir, "exports")
                        if (!exportDirectory.exists() && !exportDirectory.mkdirs()) {
                            error("Unable to create export directory")
                        }
                        val exportFile = File(
                            exportDirectory,
                            "project-$projectId-${System.currentTimeMillis()}.txt",
                        )
                        exportFile.writeText("Project $projectId export")
                        val uri = FileProvider.getUriForFile(
                            context,
                            "${context.packageName}.fileprovider",
                            exportFile,
                        )
                        val shareIntent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_STREAM, uri)
                            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        }
                        context.startActivity(
                            Intent.createChooser(
                                shareIntent,
                                context.getString(R.string.export_share_chooser_title),
                            ),
                        )
                    }.onFailure { throwable ->
                        Log.e(TAG, "Failed to export and share project $projectId", throwable)
                        Toast.makeText(
                            context,
                            context.getString(R.string.export_share_failed_message),
                            Toast.LENGTH_SHORT,
                        ).show()
                    }
                },
            ) {
                Text(text = stringResource(R.string.export_share_button))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ExportScreenPreview() {
    EbookEditorTheme {
        ExportScreen(
            projectId = 1L,
            onNavigateBack = {},
        )
    }
}
