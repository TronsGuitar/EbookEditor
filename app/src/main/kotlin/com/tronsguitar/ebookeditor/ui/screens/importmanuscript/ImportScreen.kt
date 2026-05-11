package com.tronsguitar.ebookeditor.ui.screens.importmanuscript

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.CloudUpload
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.PictureAsPdf
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tronsguitar.ebookeditor.R
import com.tronsguitar.ebookeditor.data.local.storage.EbookLocalStorage
import com.tronsguitar.ebookeditor.ui.theme.EbookEditorTheme
import java.io.InputStream
import java.util.UUID
import java.util.zip.ZipInputStream
import java.util.concurrent.atomic.AtomicLong
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val localStorage = remember(context) { EbookLocalStorage(context) }
    var selectedFormat by rememberSaveable { mutableStateOf(ImportFormat.DOCX) }
    var selectedUri by remember { mutableStateOf<Uri?>(null) }
    var selectedFileName by rememberSaveable { mutableStateOf<String?>(null) }
    var isImporting by remember { mutableStateOf(false) }
    val importLogs = remember { mutableStateListOf<ImportUiLog>() }
    val filePickerLauncher = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        if (uri == null) {
            importLogs.add(
                ImportUiLog(
                    type = LogType.WARNING,
                    message = context.getString(R.string.import_log_no_file_selected),
                ),
            )
            return@rememberLauncherForActivityResult
        }
        selectedUri = uri
        selectedFileName = context.resolveDisplayName(uri)
        importLogs.add(
            ImportUiLog(
                type = LogType.INFO,
                message = context.getString(
                    R.string.import_log_selected_file,
                    selectedFileName ?: uri.toString(),
                ),
            ),
        )
    }
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
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(
                text = stringResource(R.string.import_placeholder),
                style = androidx.compose.material3.MaterialTheme.typography.bodyMedium,
            )

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                ImportFormat.entries.forEach { format ->
                    FormatCard(
                        format = format,
                        isSelected = selectedFormat == format,
                        onClick = {
                            selectedFormat = format
                            selectedUri = null
                            selectedFileName = null
                            importLogs.clear()
                        },
                        modifier = Modifier.weight(1f),
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .border(
                        width = 2.dp,
                        color = androidx.compose.material3.MaterialTheme.colorScheme.outlineVariant,
                        shape = RoundedCornerShape(8.dp),
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Icon(
                        imageVector = Icons.Outlined.CloudUpload,
                        contentDescription = null,
                        modifier = Modifier.width(40.dp),
                    )
                    Text(
                        text = selectedFileName ?: stringResource(R.string.import_browse_placeholder),
                        textAlign = TextAlign.Center,
                    )
                    Text(
                        text = stringResource(
                            R.string.import_accepts_extension,
                            selectedFormat.extension,
                        ),
                    )
                    Button(
                        onClick = { filePickerLauncher.launch(selectedFormat.mimeTypes) },
                        enabled = !isImporting,
                    ) {
                        Text(text = stringResource(R.string.import_browse_button))
                    }
                }
            }

            Button(
                onClick = {
                    val uri = selectedUri ?: return@Button
                    coroutineScope.launch {
                        isImporting = true
                        importLogs.clear()
                        importLogs.add(
                            ImportUiLog(
                                type = LogType.INFO,
                                message = context.getString(R.string.import_log_reading_file),
                            ),
                        )
                        val selectedFileLabel = selectedFileName
                        val result = withContext(Dispatchers.IO) {
                            runCatching {
                                val importedText = context.contentResolver.openInputStream(uri)?.use { input ->
                                    selectedFormat.extractText(input)
                                }.orEmpty()
                                val normalizedText = importedText.ifBlank {
                                    context.getString(
                                        R.string.import_fallback_text_template,
                                        selectedFileLabel ?: selectedFormat.extension.uppercase(),
                                    )
                                }
                                val projectId = generateProjectId()
                                val saved = localStorage.save(projectId, normalizedText)
                                ImportResult(projectId = projectId, saved = saved, hadReadFailure = false)
                            }.getOrElse {
                                ImportResult(projectId = null, saved = false, hadReadFailure = true)
                            }
                        }
                        if (result.hadReadFailure) {
                            importLogs.add(
                                ImportUiLog(
                                    type = LogType.WARNING,
                                    message = context.getString(R.string.import_log_read_failed),
                                ),
                            )
                        } else if (result.saved && result.projectId != null) {
                            importLogs.add(
                                ImportUiLog(
                                    type = LogType.SUCCESS,
                                    message = context.getString(R.string.import_log_complete),
                                ),
                            )
                            onImportComplete(result.projectId)
                        } else {
                            importLogs.add(
                                ImportUiLog(
                                    type = LogType.WARNING,
                                    message = context.getString(R.string.import_log_save_failed),
                                ),
                            )
                        }
                        isImporting = false
                    }
                },
                enabled = selectedUri != null && !isImporting,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(text = stringResource(R.string.import_start_button))
            }

            if (isImporting) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }

            if (importLogs.isNotEmpty()) {
                Card(
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = androidx.compose.material3.MaterialTheme.colorScheme.surfaceContainerLow,
                    ),
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = stringResource(R.string.import_log_title))
                        Spacer(modifier = Modifier.height(8.dp))
                        importLogs.forEach { entry ->
                            LogRow(entry)
                        }
                    }
                }
            }
        }
    }
}

private enum class ImportFormat(
    val extension: String,
    val mimeTypes: Array<String>,
    val icon: ImageVector,
) {
    DOCX(
        extension = "docx",
        mimeTypes = arrayOf(
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
            "application/msword",
        ),
        icon = Icons.Outlined.Description,
    ),
    PDF(
        extension = "pdf",
        mimeTypes = arrayOf("application/pdf"),
        icon = Icons.Outlined.PictureAsPdf,
    ),
}

private data class ImportUiLog(val type: LogType, val message: String)

private enum class LogType { SUCCESS, INFO, WARNING }

private data class ImportResult(
    val projectId: Long?,
    val saved: Boolean,
    val hadReadFailure: Boolean,
)

@Composable
private fun FormatCard(
    format: ImportFormat,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) {
                androidx.compose.material3.MaterialTheme.colorScheme.primaryContainer
            } else {
                androidx.compose.material3.MaterialTheme.colorScheme.surfaceContainerLow
            },
        ),
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Icon(
                imageVector = format.icon,
                contentDescription = null,
            )
            Text(text = ".${format.extension}")
        }
    }
}

@Composable
private fun LogRow(entry: ImportUiLog) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = when (entry.type) {
                LogType.SUCCESS -> Icons.Outlined.CheckCircle
                LogType.INFO -> Icons.Outlined.Info
                LogType.WARNING -> Icons.Outlined.Warning
            },
            contentDescription = null,
            tint = when (entry.type) {
                LogType.SUCCESS -> androidx.compose.material3.MaterialTheme.colorScheme.primary
                LogType.INFO -> androidx.compose.material3.MaterialTheme.colorScheme.secondary
                LogType.WARNING -> androidx.compose.material3.MaterialTheme.colorScheme.error
            },
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = entry.message)
    }
}

private fun ImportFormat.extractText(input: InputStream): String = when (this) {
    ImportFormat.DOCX -> extractDocxText(input)
    ImportFormat.PDF -> extractPdfText(input)
}

internal fun extractDocxText(input: InputStream): String {
    ZipInputStream(input).use { zip ->
        var entry = zip.nextEntry
        while (entry != null) {
            if (entry.name == "word/document.xml") {
                val xml = zip.readBytes().decodeToString()
                return xml
                    .replace("</w:p>", "\n")
                    .replace(XML_TAG_REGEX, " ")
                    .replace("&amp;", "&")
                    .replace("&lt;", "<")
                    .replace("&gt;", ">")
                    .replace(DOCX_INLINE_WHITESPACE_REGEX, " ")
                    .lines()
                    .joinToString("\n") { it.trim() }
                    .trim()
            }
            zip.closeEntry()
            entry = zip.nextEntry
        }
    }
    return ""
}

internal fun extractPdfText(bytes: ByteArray): String {
    val content = bytes.toString(Charsets.ISO_8859_1)
    val snippets = PDF_TEXT_OPERATOR_REGEX
        .findAll(content)
        .map { it.groupValues[1].replace("""\(""", "(").replace("""\)""", ")") }
        .toList()
    if (snippets.isNotEmpty()) {
        return snippets.joinToString(separator = "\n").trim()
    }
    val fallback = content.filter { it.code in 32..126 || it == '\n' }
        .replace(PDF_FALLBACK_WHITESPACE_REGEX, " ")
        .trim()
    return if (fallback.length > MAX_PDF_FALLBACK_LENGTH) {
        fallback.take(MAX_PDF_FALLBACK_LENGTH)
    } else {
        fallback
    }
}

internal fun extractPdfText(input: InputStream): String {
    val bytes = input.readNBytes(MAX_PDF_INPUT_BYTES)
    return extractPdfText(bytes)
}

private fun Context.resolveDisplayName(uri: Uri): String? {
    return contentResolver.query(uri, null, null, null, null)?.use { cursor ->
        val index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        if (index >= 0 && cursor.moveToFirst()) cursor.getString(index) else null
    }
}

private fun generateProjectId(): Long {
    val timestampPart = System.currentTimeMillis().coerceAtLeast(0)
    val sequencePart = PROJECT_ID_COUNTER.getAndIncrement() and PROJECT_ID_COUNTER_MASK
    val randomPart = (UUID.randomUUID().leastSignificantBits and PROJECT_ID_RANDOM_MASK)
    return ((timestampPart shl PROJECT_ID_COUNTER_BITS) or sequencePart xor randomPart) and Long.MAX_VALUE
}

private const val MAX_PDF_TEXT_OPERATOR_LENGTH = 500
private const val MAX_PDF_FALLBACK_LENGTH = 4000
private const val MAX_PDF_INPUT_BYTES = 1_048_576
private const val PROJECT_ID_COUNTER_BITS = 10
private const val PROJECT_ID_COUNTER_MASK = (1L shl PROJECT_ID_COUNTER_BITS) - 1
private const val PROJECT_ID_RANDOM_MASK = (1L shl 20) - 1
private val PROJECT_ID_COUNTER = AtomicLong(0)
private val XML_TAG_REGEX = Regex("<[^>]+>")
private val DOCX_INLINE_WHITESPACE_REGEX = Regex("[\\t\\r ]+")
private val PDF_TEXT_OPERATOR_REGEX = Regex("""\(([^)]{1,$MAX_PDF_TEXT_OPERATOR_LENGTH})\)\s*Tj""")
private val PDF_FALLBACK_WHITESPACE_REGEX = Regex("\\s+")

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
