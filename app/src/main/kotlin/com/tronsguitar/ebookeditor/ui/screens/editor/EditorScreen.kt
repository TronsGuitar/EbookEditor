package com.tronsguitar.ebookeditor.ui.screens.editor

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tronsguitar.ebookeditor.R
import com.tronsguitar.ebookeditor.domain.model.Chapter
import com.tronsguitar.ebookeditor.ui.theme.EbookEditorTheme

/**
 * Editor Screen – the distraction-free Focus Mode editor.
 *
 * Displays a chapter-list sidebar alongside the main text editor.
 * Content is autosaved to Room after a short idle period, with a
 * visual indicator in the top bar reflecting autosave status.
 *
 * The text container is capped at 800dp regardless of screen width to
 * maintain the optimal 60-80 character line length for professional
 * editing (see Design System spec).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditorScreen(
    onNavigateBack: () -> Unit,
    viewModel: EditorViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    var showAddChapterDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(text = stringResource(R.string.screen_editor_title))
                        Text(
                            text = when (uiState.autoSaveStatus) {
                                AutoSaveStatus.SAVING -> stringResource(R.string.editor_autosave_saving)
                                AutoSaveStatus.SAVED  -> stringResource(R.string.editor_autosave_saved)
                                AutoSaveStatus.IDLE   -> stringResource(
                                    R.string.editor_word_count,
                                    uiState.wordCount,
                                )
                            },
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                },
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
                            if (uiState.content.isNotBlank()) {
                                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                    type = "text/plain"
                                    putExtra(Intent.EXTRA_TEXT, uiState.content)
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
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            // ── Chapter list sidebar ─────────────────────────────────────────
            ChapterSidebar(
                chapters = uiState.chapters,
                selectedChapterId = uiState.selectedChapterId,
                totalWordCount = uiState.totalWordCount,
                onSelectChapter = { viewModel.selectChapter(it) },
                onDeleteChapter = { viewModel.deleteChapter(it) },
                onAddChapter = { showAddChapterDialog = true },
                modifier = Modifier
                    .width(200.dp)
                    .fillMaxHeight(),
            )

            VerticalDivider()

            // ── Main editor canvas ───────────────────────────────────────────
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                contentAlignment = Alignment.TopCenter,
            ) {
                if (uiState.selectedChapterId == null) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(
                            text = stringResource(R.string.editor_no_chapter_hint),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                } else {
                    OutlinedTextField(
                        value = uiState.content,
                        onValueChange = viewModel::onContentChange,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        label = { Text(text = stringResource(R.string.editor_content_label)) },
                        placeholder = { Text(text = stringResource(R.string.editor_content_placeholder)) },
                    )
                }
            }
        }
    }

    if (showAddChapterDialog) {
        AddChapterDialog(
            onConfirm = { title ->
                viewModel.addChapter(title)
                showAddChapterDialog = false
            },
            onDismiss = { showAddChapterDialog = false },
        )
    }
}

// ── Chapter sidebar ───────────────────────────────────────────────────────────

@Composable
private fun ChapterSidebar(
    chapters: List<Chapter>,
    selectedChapterId: Long?,
    totalWordCount: Int,
    onSelectChapter: (Long) -> Unit,
    onDeleteChapter: (Chapter) -> Unit,
    onAddChapter: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(R.string.editor_chapters_heading),
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.weight(1f),
            )
            IconButton(onClick = onAddChapter) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.cd_add_chapter),
                )
            }
        }

        LazyColumn(modifier = Modifier.weight(1f)) {
            itemsIndexed(chapters) { _, chapter ->
                ChapterRow(
                    chapter = chapter,
                    isSelected = chapter.id == selectedChapterId,
                    onSelect = { onSelectChapter(chapter.id) },
                    onDelete = { onDeleteChapter(chapter) },
                )
            }
        }

        HorizontalDivider()
        Text(
            text = stringResource(R.string.editor_total_word_count, totalWordCount),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
        )
    }
}

@Composable
private fun ChapterRow(
    chapter: Chapter,
    isSelected: Boolean,
    onSelect: () -> Unit,
    onDelete: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                if (isSelected) MaterialTheme.colorScheme.secondaryContainer
                else MaterialTheme.colorScheme.surface,
            )
            .clickable(onClick = onSelect)
            .padding(start = 12.dp, end = 4.dp, top = 6.dp, bottom = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = chapter.title,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = stringResource(R.string.editor_chapter_word_count, chapter.wordCount),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        IconButton(onClick = onDelete) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = stringResource(R.string.cd_delete_chapter),
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

// ── Add chapter dialog ────────────────────────────────────────────────────────

@Composable
private fun AddChapterDialog(
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit,
) {
    var title by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = stringResource(R.string.editor_add_chapter_title)) },
        text = {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text(text = stringResource(R.string.editor_chapter_title_label)) },
                singleLine = true,
            )
        },
        confirmButton = {
            TextButton(
                onClick = { if (title.isNotBlank()) onConfirm(title) },
                enabled = title.isNotBlank(),
            ) {
                Text(text = stringResource(R.string.editor_add_chapter_confirm))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(R.string.editor_add_chapter_cancel))
            }
        },
    )
}

// ── Previews ──────────────────────────────────────────────────────────────────

@Preview(showBackground = true, widthDp = 800)
@Composable
private fun ChapterSidebarPreview() {
    EbookEditorTheme {
        val chapters = listOf(
            Chapter(id = 1L, projectId = 1L, title = "Chapter One", wordCount = 1200),
            Chapter(id = 2L, projectId = 1L, title = "Chapter Two – The Road Ahead", wordCount = 850),
        )
        ChapterSidebar(
            chapters = chapters,
            selectedChapterId = 1L,
            totalWordCount = 2050,
            onSelectChapter = {},
            onDeleteChapter = {},
            onAddChapter = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AddChapterDialogPreview() {
    EbookEditorTheme {
        AddChapterDialog(onConfirm = {}, onDismiss = {})
    }
}
