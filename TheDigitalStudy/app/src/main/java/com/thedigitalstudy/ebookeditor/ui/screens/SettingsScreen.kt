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
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen() {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Book Info", "AI Disclosure", "Cover Art")

    Scaffold(
        topBar = {
            TopAppBar(
                title  = { Text("Project Settings") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
            )
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor   = MaterialTheme.colorScheme.surfaceContainerLow,
            ) {
                tabs.forEachIndexed { i, title ->
                    Tab(
                        selected = selectedTab == i,
                        onClick  = { selectedTab = i },
                        text     = { Text(title, style = MaterialTheme.typography.labelLarge) },
                    )
                }
            }
            when (selectedTab) {
                0 -> BookInfoTab()
                1 -> AiDisclosureTab()
                2 -> CoverArtTab()
            }
        }
    }
}

// ── Tab 1 — Book Info ─────────────────────────────────────────────────────────

@Composable
private fun BookInfoTab() {
    var title       by remember { mutableStateOf("The Forgotten Archive") }
    var subtitle    by remember { mutableStateOf("") }
    var author      by remember { mutableStateOf("Jane Ashford") }
    var isbn        by remember { mutableStateOf("") }
    var genre       by remember { mutableStateOf("Literary Fiction") }
    var keywords    by remember { mutableStateOf("archive, history, mystery, literary") }
    var description by remember { mutableStateOf("A story about memory, secrets, and the archives we keep.") }

    val keywordCount = keywords.split(",").count { it.isNotBlank() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        FieldInput("Title *",               title,    { title = it })
        FieldInput("Subtitle",              subtitle, { subtitle = it })
        FieldInput("Author Name *",         author,   { author = it })
        FieldInput("ISBN / ASIN",           isbn,     { isbn = it })
        FieldInput("Genre / Category *",    genre,    { genre = it })

        Column {
            FieldInput("Keywords (comma-separated)", keywords, { keywords = it })
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    "$keywordCount keywords",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                if (keywordCount > 7) {
                    Text(
                        "Warning: possible keyword stuffing",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.error,
                    )
                }
            }
        }

        OutlinedTextField(
            value         = description,
            onValueChange = { description = it },
            label         = { Text("Book Description") },
            modifier      = Modifier.fillMaxWidth(),
            minLines      = 4,
        )

        Button(
            onClick  = {},
            modifier = Modifier.fillMaxWidth(),
            shape    = RoundedCornerShape(4.dp),
        ) { Text("Save Book Info") }
    }
}

// ── Tab 2 — AI Disclosure (D2D 2026 mandatory) ────────────────────────────────

@Composable
private fun AiDisclosureTab() {
    var aiWriting    by remember { mutableStateOf(false) }
    var aiEditing    by remember { mutableStateOf(true) }
    var aiCoverArt   by remember { mutableStateOf(false) }
    var disclosure   by remember {
        mutableStateOf(
            "AI tools were used for editing and proofreading assistance only. " +
            "All creative content is original."
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        // Info banner
        Card(
            shape  = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            ),
        ) {
            Row(
                modifier              = Modifier.padding(12.dp),
                verticalAlignment     = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Icon(
                    Icons.Outlined.Info,
                    contentDescription = null,
                    tint     = MaterialTheme.colorScheme.onSecondaryContainer,
                    modifier = Modifier.size(16.dp).padding(top = 2.dp),
                )
                Text(
                    "D2D 2026 requires authors to disclose any use of AI tools " +
                    "in creation. Accurate disclosure is required for submission.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                )
            }
        }

        Text("AI Usage", style = MaterialTheme.typography.titleSmall)
        DisclosureToggle("AI-generated writing / prose",         aiWriting,  { aiWriting  = it })
        DisclosureToggle("AI-assisted editing or proofreading",  aiEditing,  { aiEditing  = it })
        DisclosureToggle("AI-generated cover art or images",     aiCoverArt, { aiCoverArt = it })

        OutlinedTextField(
            value         = disclosure,
            onValueChange = { disclosure = it },
            label         = { Text("Disclosure Statement") },
            modifier      = Modifier.fillMaxWidth(),
            minLines      = 4,
            placeholder   = { Text("Describe how AI was used in this work…") },
        )

        Button(
            onClick  = {},
            modifier = Modifier.fillMaxWidth(),
            shape    = RoundedCornerShape(4.dp),
        ) { Text("Save AI Disclosure") }
    }
}

@Composable
private fun DisclosureToggle(label: String, checked: Boolean, onToggle: (Boolean) -> Unit) {
    Row(
        modifier              = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment     = Alignment.CenterVertically,
    ) {
        Text(label, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.weight(1f))
        Switch(checked = checked, onCheckedChange = onToggle)
    }
}

// ── Tab 3 — Cover Art ─────────────────────────────────────────────────────────

@Composable
private fun CoverArtTab() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement   = Arrangement.spacedBy(16.dp),
        horizontalAlignment   = Alignment.CenterHorizontally,
    ) {
        // Cover placeholder
        Surface(
            modifier       = Modifier.width(200.dp).height(280.dp),
            shape          = RoundedCornerShape(4.dp),
            color          = MaterialTheme.colorScheme.surfaceContainerHigh,
            tonalElevation = 2.dp,
        ) {
            Box(contentAlignment = Alignment.Center) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Icon(
                        Icons.Outlined.Image, null,
                        modifier = Modifier.size(40.dp),
                        tint     = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Text(
                        "No cover uploaded",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }

        // D2D requirements checklist
        Card(
            shape  = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerLow
            ),
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("D2D Cover Requirements", style = MaterialTheme.typography.titleSmall)
                Spacer(Modifier.height(8.dp))
                RequirementRow("Minimum 1600 × 2400 px", met = true)
                RequirementRow("RGB colour mode",         met = true)
                RequirementRow("≥ 300 DPI",               met = true)
                RequirementRow("JPEG or PNG format",      met = true)
                RequirementRow("Bleed-edge compliance",   met = false)
            }
        }

        OutlinedButton(
            onClick  = {},
            modifier = Modifier.fillMaxWidth(),
            shape    = RoundedCornerShape(4.dp),
        ) {
            Icon(Icons.Outlined.FileUpload, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text("Upload Cover Art")
        }
    }
}

@Composable
private fun RequirementRow(label: String, met: Boolean) {
    Row(
        modifier          = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Icon(
            imageVector        = if (met) Icons.Outlined.CheckCircle
                                 else     Icons.Outlined.RadioButtonUnchecked,
            contentDescription = null,
            modifier           = Modifier.size(16.dp),
            tint               = if (met) MaterialTheme.colorScheme.primary
                                 else     MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(label, style = MaterialTheme.typography.bodySmall)
    }
}

// ── Shared helper ─────────────────────────────────────────────────────────────

@Composable
private fun FieldInput(label: String, value: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value         = value,
        onValueChange = onValueChange,
        label         = { Text(label) },
        modifier      = Modifier.fillMaxWidth(),
        singleLine    = true,
    )
}
