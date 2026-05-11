package com.thedigitalstudy.ebookeditor.ui.screens

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
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExportScreen() {
    var selectedFormat   by remember { mutableStateOf(ExportFormat.EPUB) }
    var showSuccessDialog by remember { mutableStateOf(false) }

    val qcItems = remember {
        mutableStateListOf(
            QcItem("Reflowable layout geometry",             true),
            QcItem("EPUB 3.0 structural validity",           true),
            QcItem("AI content disclosure included",         true),
            QcItem("Cover art resolution (≥ 300 DPI)",       true),
            QcItem("Metadata completeness (title / author)", true),
            QcItem("No keyword stuffing detected",           false),
            QcItem("About the Author endmatter",             true),
            QcItem("'Also By' page generated",               false),
        )
    }
    val passed = qcItems.count { it.passed }

    Scaffold(
        topBar = {
            TopAppBar(
                title  = { Text("Export & Publish") },
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
            // Format selector
            Text("Export Format", style = MaterialTheme.typography.titleMedium)
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                ExportFormat.entries.forEach { fmt ->
                    ExportFormatCard(
                        format     = fmt,
                        isSelected = selectedFormat == fmt,
                        onClick    = { selectedFormat = fmt },
                        modifier   = Modifier.weight(1f),
                    )
                }
            }

            // D2D QC report
            Card(
                shape  = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerLow
                ),
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier              = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment     = Alignment.CenterVertically,
                    ) {
                        Text("D2D Quality Check", style = MaterialTheme.typography.titleSmall)
                        val allPass = passed == qcItems.size
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = if (allPass) MaterialTheme.colorScheme.primaryContainer
                                    else         MaterialTheme.colorScheme.errorContainer,
                        ) {
                            Text(
                                "$passed/${qcItems.size} Passed",
                                style    = MaterialTheme.typography.labelSmall,
                                color    = if (allPass) MaterialTheme.colorScheme.onPrimaryContainer
                                           else         MaterialTheme.colorScheme.onErrorContainer,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            )
                        }
                    }
                    Spacer(Modifier.height(12.dp))
                    qcItems.forEach { QcRow(it) }
                }
            }

            // Export settings
            Card(
                shape  = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerLow
                ),
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Export Settings", style = MaterialTheme.typography.titleSmall)
                    Spacer(Modifier.height(4.dp))
                    ToggleRow("Include Table of Contents", true)
                    ToggleRow("Generate Endmatter",        true)
                    ToggleRow("Embed Cover Art",            true)
                    ToggleRow("D2D Compliance Mode",        true)
                }
            }

            Button(
                onClick  = { showSuccessDialog = true },
                modifier = Modifier.fillMaxWidth(),
                shape    = RoundedCornerShape(4.dp),
            ) {
                Icon(Icons.Outlined.Publish, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Export as ${selectedFormat.label}")
            }
        }
    }

    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = { showSuccessDialog = false },
            icon             = { Icon(Icons.Outlined.CheckCircle, contentDescription = null) },
            title            = { Text("Export Ready") },
            text             = {
                Text(
                    "Manuscript exported as ${selectedFormat.label} and ready " +
                    "for submission to Draft2Digital."
                )
            },
            confirmButton    = {
                Button(onClick = { showSuccessDialog = false }) { Text("Done") }
            },
        )
    }
}

// ── Sub-components ────────────────────────────────────────────────────────────

@Composable
private fun ExportFormatCard(
    format: ExportFormat,
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
            modifier            = Modifier.padding(12.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Icon(
                format.icon, contentDescription = null,
                tint = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer
                       else            MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(
                format.label,
                style = MaterialTheme.typography.labelLarge,
                color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer
                        else            MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}

@Composable
private fun QcRow(item: QcItem) {
    Column {
        Row(
            modifier          = Modifier.fillMaxWidth().padding(vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Icon(
                imageVector        = if (item.passed) Icons.Outlined.CheckCircle
                                     else             Icons.Outlined.Cancel,
                contentDescription = null,
                modifier           = Modifier.size(18.dp),
                tint               = if (item.passed) MaterialTheme.colorScheme.primary
                                     else             MaterialTheme.colorScheme.error,
            )
            Text(item.label, style = MaterialTheme.typography.bodySmall, modifier = Modifier.weight(1f))
        }
        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
    }
}

@Composable
private fun ToggleRow(label: String, initial: Boolean) {
    var on by remember { mutableStateOf(initial) }
    Row(
        modifier              = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment     = Alignment.CenterVertically,
    ) {
        Text(label, style = MaterialTheme.typography.bodySmall)
        Switch(checked = on, onCheckedChange = { on = it })
    }
}

// ── Models ────────────────────────────────────────────────────────────────────

enum class ExportFormat(val label: String, val icon: ImageVector) {
    EPUB("EPUB 3.0",   Icons.Outlined.MenuBook),
    PDF("Print PDF",   Icons.Outlined.PictureAsPdf),
    DOCX("DOCX",       Icons.Outlined.Description),
}

data class QcItem(val label: String, val passed: Boolean)
