package com.thedigitalstudy.ebookeditor.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.thedigitalstudy.ebookeditor.data.model.Project
import com.thedigitalstudy.ebookeditor.data.model.ProjectStatus
import com.thedigitalstudy.ebookeditor.data.model.sampleProjects

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(onProjectClick: (String) -> Unit) {
    var searchQuery by remember { mutableStateOf("") }
    val projects = sampleProjects.filter {
        searchQuery.isBlank() || it.title.contains(searchQuery, ignoreCase = true)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("The Digital Study", style = MaterialTheme.typography.headlineMedium)
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                actions = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Outlined.AccountCircle, contentDescription = "Profile")
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick       = { /* TODO: new project dialog */ },
                icon          = { Icon(Icons.Outlined.Add, contentDescription = null) },
                text          = { Text("New Project") },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor  = MaterialTheme.colorScheme.onPrimary,
            )
        }
    ) { innerPadding ->
        LazyVerticalGrid(
            columns             = GridCells.Adaptive(minSize = 300.dp),
            contentPadding      = PaddingValues(
                start  = 16.dp,
                end    = 16.dp,
                top    = innerPadding.calculateTopPadding() + 8.dp,
                bottom = innerPadding.calculateBottomPadding() + 88.dp,
            ),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement   = Arrangement.spacedBy(12.dp),
        ) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                OutlinedTextField(
                    value         = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier      = Modifier.fillMaxWidth(),
                    placeholder   = { Text("Search manuscripts…") },
                    leadingIcon   = { Icon(Icons.Outlined.Search, contentDescription = null) },
                    singleLine    = true,
                    shape         = RoundedCornerShape(12.dp),
                )
            }
            item(span = { GridItemSpan(maxLineSpan) }) {
                Text(
                    text     = "Recent Projects",
                    style    = MaterialTheme.typography.titleMedium,
                    color    = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 8.dp, bottom = 4.dp),
                )
            }
            items(projects) { project ->
                ProjectCard(project = project, onClick = { onProjectClick(project.id) })
            }
        }
    }
}

// ── Project card ─────────────────────────────────────────────────────────────

@Composable
fun ProjectCard(project: Project, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape    = RoundedCornerShape(8.dp),
        colors   = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        ),
    ) {
        Row(modifier = Modifier.height(IntrinsicSize.Min)) {
            // 4 dp accent bar — the "chapter list active indicator" from the design system
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .fillMaxHeight()
                    .background(MaterialTheme.colorScheme.primary)
            )
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .weight(1f),
            ) {
                Row(
                    modifier              = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment     = Alignment.Top,
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(project.title, style = MaterialTheme.typography.headlineSmall)
                        Text(
                            project.genre,
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                    Spacer(Modifier.width(8.dp))
                    StatusChip(project.status)
                }

                Spacer(Modifier.height(12.dp))

                // Word count + progress bar
                Row(
                    modifier              = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        "${project.wordCount.formatK()} words",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Text(
                        "${(project.progress * 100).toInt()}%",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary,
                    )
                }
                Spacer(Modifier.height(4.dp))
                LinearProgressIndicator(
                    progress   = { project.progress },
                    modifier   = Modifier.fillMaxWidth(),
                    color      = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.outlineVariant,
                )

                Spacer(Modifier.height(8.dp))
                Text(
                    "Last edited ${project.lastEdited}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

@Composable
private fun StatusChip(status: ProjectStatus) {
    val (bg, fg) = when (status) {
        ProjectStatus.DRAFT       -> MaterialTheme.colorScheme.surfaceContainerHighest to
                                     MaterialTheme.colorScheme.onSurfaceVariant
        ProjectStatus.IN_PROGRESS -> MaterialTheme.colorScheme.secondaryContainer to
                                     MaterialTheme.colorScheme.onSecondaryContainer
        ProjectStatus.REVIEW      -> MaterialTheme.colorScheme.tertiaryContainer to
                                     MaterialTheme.colorScheme.onTertiaryContainer
        ProjectStatus.PUBLISHED   -> MaterialTheme.colorScheme.primaryContainer to
                                     MaterialTheme.colorScheme.onPrimaryContainer
    }
    Surface(shape = RoundedCornerShape(4.dp), color = bg) {
        Text(
            text     = status.label,
            style    = MaterialTheme.typography.labelSmall,
            color    = fg,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
        )
    }
}

private fun Int.formatK(): String =
    if (this >= 1_000) "${this / 1_000},${(this % 1_000).toString().padStart(3, '0')}"
    else toString()
