package com.thedigitalstudy.ebookeditor.ui.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImportScreen() {
    var selectedFormat by remember { mutableStateOf<ImportFormat?>(null) }
    val logMessages    = remember { mutableStateListOf<LogEntry>() }
    var hasImported    by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title  = { Text("Import Manuscript") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            // Format picker
            Text("Select File Format", style = MaterialTheme.typography.titleMedium)
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                ImportFormat.entries.forEach { fmt ->
                    FormatCard(
                        format     = fmt,
                        isSelected = selectedFormat == fmt,
                        onClick    = { selectedFormat = fmt },
                        modifier   = Modifier.weight(1f),
                    )
                }
            }

            // Drop zone
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .border(
                        width  = 2.dp,
                        color  = if (selectedFormat != null) MaterialTheme.colorScheme.primary
                                 else MaterialTheme.colorScheme.outlineVariant,
                        shape  = RoundedCornerShape(8.dp),
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Icon(
                        Icons.Outlined.CloudUpload,
                        contentDescription = null,
                        modifier = Modifier.size(40.dp),
                        tint     = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Text(
                        "Drop file here or tap to browse",
                        style     = MaterialTheme.typography.bodyMedium,
                        color     = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                    )
                    if (selectedFormat != null) {
                        Text(
                            "Accepts .${selectedFormat!!.extension}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary,
                        )
                    }
                }
            }

            // Import button (simulates the pipeline)
            Button(
                onClick = {
                    logMessages.clear()
                    logMessages.addAll(simulatedLog(selectedFormat!!))
                    hasImported = true
                },
                modifier = Modifier.fillMaxWidth(),
                enabled  = selectedFormat != null,
                shape    = RoundedCornerShape(4.dp),
            ) {
                Icon(Icons.Outlined.AutoAwesome, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Import & Analyze")
            }

            // Processing log
            if (logMessages.isNotEmpty()) {
                Card(
                    shape  = RoundedCornerShape(4.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerLow
                    ),
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Import Log", style = MaterialTheme.typography.titleSmall)
                        Spacer(Modifier.height(8.dp))
                        logMessages.forEach { entry -> LogRow(entry) }
                    }
                }
            }
        }
    }
}

// ── Sub-components ────────────────────────────────────────────────────────────

@Composable
private fun FormatCard(
    format: ImportFormat,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        onClick  = onClick,
        modifier = modifier,
        shape    = RoundedCornerShape(8.dp),
        colors   = CardDefaults.cardColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer
                             else            MaterialTheme.colorScheme.surfaceContainerLow
        ),
    ) {
        Column(
            modifier            = Modifier.padding(16.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Icon(
                imageVector        = format.icon,
                contentDescription = null,
                tint               = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer
                                     else            MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(
                text  = format.label,
                style = MaterialTheme.typography.labelLarge,
                color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer
                        else            MaterialTheme.colorScheme.onSurface,
            )
            Text(
                ".${format.extension}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun LogRow(entry: LogEntry) {
    Row(
        modifier          = Modifier.fillMaxWidth().padding(vertical = 3.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector        = when (entry.type) {
                LogType.SUCCESS -> Icons.Outlined.CheckCircle
                LogType.INFO    -> Icons.Outlined.Info
                LogType.WARNING -> Icons.Outlined.Warning
            },
            contentDescription = null,
            modifier           = Modifier.size(14.dp),
            tint               = when (entry.type) {
                LogType.SUCCESS -> MaterialTheme.colorScheme.primary
                LogType.INFO    -> MaterialTheme.colorScheme.secondary
                LogType.WARNING -> MaterialTheme.colorScheme.error
            },
        )
        Spacer(Modifier.width(8.dp))
        Text(
            entry.message,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

// ── Models ────────────────────────────────────────────────────────────────────

enum class ImportFormat(val label: String, val extension: String, val icon: ImageVector) {
    DOCX("Word Document", "docx", Icons.Outlined.Description),
    PDF("PDF File",       "pdf",  Icons.Outlined.PictureAsPdf),
}

data class LogEntry(val type: LogType, val message: String)
enum class LogType { SUCCESS, INFO, WARNING }

private fun simulatedLog(format: ImportFormat) = listOf(
    LogEntry(LogType.INFO,    "Reading ${format.label}…"),
    LogEntry(LogType.SUCCESS, "Detected 5 chapters, 2 sub-headings"),
    LogEntry(LogType.SUCCESS, "Extracted 42,500 words"),
    LogEntry(LogType.WARNING, "Removed 23 hard line breaks"),
    LogEntry(LogType.SUCCESS, "Normalised 8 heading styles → H1/H2"),
    LogEntry(LogType.INFO,    "Analysing semantic structure…"),
    LogEntry(LogType.SUCCESS, "Body text converted to reflowable format"),
    LogEntry(LogType.WARNING, "2 fixed-width tables detected — manual review recommended"),
    LogEntry(LogType.SUCCESS, "Import complete. Project ready for editing."),
)
