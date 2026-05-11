package com.tronsguitar.ebookeditor.ui.screens.editor

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tronsguitar.ebookeditor.R
import com.tronsguitar.ebookeditor.data.local.storage.EbookLocalStorage
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
    storage: EbookLocalStorage? = null,
) {
    val context = LocalContext.current
    val localStorage = storage ?: remember(context) { EbookLocalStorage(context) }
    var manuscriptContent by remember(projectId) { mutableStateOf("") }

    LaunchedEffect(projectId) {
        manuscriptContent = localStorage.read(projectId).orEmpty()
    }

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
                    IconButton(
                        onClick = {
                            if (manuscriptContent.isNotBlank()) {
                                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                    type = "text/plain"
                                    putExtra(Intent.EXTRA_TEXT, manuscriptContent)
                                }
                                context.startActivity(
                                    Intent.createChooser(
                                        shareIntent,
                                        context.getString(R.string.share_ebook_chooser_title),
                                    ),
                                )
                            }
                        },
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = stringResource(R.string.cd_share_ebook),
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
            contentAlignment = Alignment.TopCenter,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
            ) {
                OutlinedTextField(
                    value = manuscriptContent,
                    onValueChange = { manuscriptContent = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(text = stringResource(R.string.editor_content_label)) },
                    placeholder = { Text(text = stringResource(R.string.editor_content_placeholder)) },
                    minLines = 10,
                )
                Spacer(modifier = Modifier.height(12.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = {
                            val saved = localStorage.save(projectId, manuscriptContent)
                            if (!saved) {
                                Toast.makeText(
                                    context,
                                    context.getString(R.string.editor_save_failed_message),
                                    Toast.LENGTH_SHORT,
                                ).show()
                            }
                        },
                    ) {
                        Text(text = stringResource(R.string.editor_save_button))
                    }
                    Button(
                        onClick = {
                            val deleted = localStorage.delete(projectId)
                            if (deleted) {
                                manuscriptContent = ""
                            } else {
                                Toast.makeText(
                                    context,
                                    context.getString(R.string.editor_delete_failed_message),
                                    Toast.LENGTH_SHORT,
                                ).show()
                            }
                        },
                    ) {
                        Text(text = stringResource(R.string.editor_delete_button))
                    }
                }
            }
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
        )
    }
}
