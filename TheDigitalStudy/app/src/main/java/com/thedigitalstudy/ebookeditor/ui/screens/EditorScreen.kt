package com.thedigitalstudy.ebookeditor.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.FormatListBulleted
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.*
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowWidthSizeClass
import com.thedigitalstudy.ebookeditor.data.model.sampleProjects

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditorScreen(projectId: String, onBack: () -> Unit) {
    val project = sampleProjects.find { it.id == projectId } ?: sampleProjects.first()

    val adaptiveInfo = currentWindowAdaptiveInfo()
    val widthClass   = adaptiveInfo.windowSizeClass.windowWidthSizeClass
    val showSidebar  = widthClass == WindowWidthSizeClass.EXPANDED ||
                       widthClass == WindowWidthSizeClass.MEDIUM

    var selectedChapter by remember { mutableIntStateOf(0) }
    var editorText by remember {
        mutableStateOf(
            "Chapter One\n\n" +
            "The archive had been closed for thirty years when Mira first pushed open its iron door. " +
            "Dust motes swirled in the pale morning light, suspended like memories too fragile to settle. " +
            "She stepped inside, her breath catching on the smell of old paper and forgotten certainties.\n\n" +
            "The shelves stretched upward into a darkness that the overhead lights seemed unwilling to challenge. " +
            "Thousands of spines, most unreadable, watched her entrance with the passive indifference of the long-neglected."
        )
    }

    val chapters = listOf(
        "Chapter 1 — The Iron Door",
        "Chapter 2 — First Light",
        "Chapter 3 — The Cataloguer",
        "Chapter 4 — Restricted Access",
        "Chapter 5 — What Was Sealed",
    )

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "Back")
                    }
                },
                title = {
                    Column {
                        Text(project.title, style = MaterialTheme.typography.titleMedium)
                        Text(
                            "${project.wordCount.formatK()} words · Auto-saved",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer
                ),
                actions = {
                    IconButton(onClick = {}) {
                        Icon(Icons.AutoMirrored.Outlined.FormatListBulleted, contentDescription = "Outline")
                    }
                    IconButton(onClick = {}) {
                        Icon(Icons.Outlined.MoreVert, contentDescription = "More")
                    }
                }
            )
        }
    ) { padding ->
        Row(modifier = Modifier.fillMaxSize().padding(padding)) {
            if (showSidebar) {
                ChapterListPane(
                    chapters        = chapters,
                    selectedIndex   = selectedChapter,
                    onChapterSelect = { selectedChapter = it },
                    modifier        = Modifier.width(240.dp),
                )
            }
            EditorPane(
                text     = editorText,
                onChange = { editorText = it },
                modifier = Modifier.weight(1f),
            )
        }
    }
}

// ── Chapter list sidebar ──────────────────────────────────────────────────────

@Composable
private fun ChapterListPane(
    chapters: List<String>,
    selectedIndex: Int,
    onChapterSelect: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier       = modifier.fillMaxHeight(),
        color          = MaterialTheme.colorScheme.surfaceContainerLow,
        tonalElevation = 1.dp,
    ) {
        Column {
            Text(
                text     = "CHAPTERS",
                style    = MaterialTheme.typography.labelSmall,
                color    = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
            )
            chapters.forEachIndexed { index, title ->
                ChapterRow(
                    title      = title,
                    isSelected = index == selectedIndex,
                    onClick    = { onChapterSelect(index) },
                )
            }
            Spacer(Modifier.weight(1f))
            OutlinedButton(
                onClick  = {},
                modifier = Modifier.fillMaxWidth().padding(12.dp),
                shape    = RoundedCornerShape(4.dp),
            ) {
                Icon(Icons.Outlined.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(4.dp))
                Text("Add Chapter")
            }
        }
    }
}

@Composable
private fun ChapterRow(title: String, isSelected: Boolean, onClick: () -> Unit) {
    Row(
        modifier          = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .background(
                if (isSelected) MaterialTheme.colorScheme.surfaceContainerHigh
                else Color.Transparent
            )
            .padding(vertical = 12.dp, horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // 3 dp active-state bar — per design system spec
        Box(
            modifier = Modifier
                .width(3.dp)
                .height(20.dp)
                .background(
                    if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                    RoundedCornerShape(2.dp),
                )
        )
        Spacer(Modifier.width(12.dp))
        Text(
            text  = title,
            style = MaterialTheme.typography.bodySmall,
            color = if (isSelected) MaterialTheme.colorScheme.onSurface
                    else MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

// ── Editor pane ───────────────────────────────────────────────────────────────

@Composable
private fun EditorPane(
    text: String,
    onChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier          = modifier
            .fillMaxHeight()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment  = Alignment.TopCenter,
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 800.dp)   // Golden-ratio column cap from design system
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 32.dp),
        ) {
            // "Paper-white" editor surface with soft shadow
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(elevation = 2.dp, shape = RoundedCornerShape(4.dp)),
                shape    = RoundedCornerShape(4.dp),
                color    = MaterialTheme.colorScheme.surfaceContainerLowest,
            ) {
                BasicTextField(
                    value         = text,
                    onValueChange = onChange,
                    modifier      = Modifier
                        .fillMaxWidth()
                        .padding(32.dp)
                        .heightIn(min = 600.dp),
                    textStyle     = MaterialTheme.typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.onSurface
                    ),
                    cursorBrush   = SolidColor(MaterialTheme.colorScheme.primary),
                )
            }
        }
    }
}

private fun Int.formatK(): String =
    if (this >= 1_000) "${this / 1_000},${(this % 1_000).toString().padStart(3, '0')}"
    else toString()
