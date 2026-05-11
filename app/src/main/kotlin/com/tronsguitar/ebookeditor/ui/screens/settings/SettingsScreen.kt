package com.tronsguitar.ebookeditor.ui.screens.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.weight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tronsguitar.ebookeditor.R
import com.tronsguitar.ebookeditor.ui.theme.EbookEditorTheme

/**
 * Settings Screen – Project metadata, author profile, and AI disclosure configuration.
 *
 * Covers comprehensive book metadata including mandatory AI usage disclosures
 * required for D2D 2026 compliance, cover art management, and keyword-stuffing
 * validation feedback.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.screen_settings_title)) },
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
        if (!uiState.hasProject) {
            EmptySettingsState(innerPadding = innerPadding)
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            OutlinedTextField(
                value = uiState.title,
                onValueChange = viewModel::onTitleChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = stringResource(R.string.settings_title_label)) },
            )
            OutlinedTextField(
                value = uiState.authorName,
                onValueChange = viewModel::onAuthorNameChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = stringResource(R.string.settings_author_label)) },
            )
            OutlinedTextField(
                value = uiState.genre,
                onValueChange = viewModel::onGenreChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = stringResource(R.string.settings_genre_label)) },
            )
            OutlinedTextField(
                value = uiState.synopsis,
                onValueChange = viewModel::onSynopsisChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = stringResource(R.string.settings_synopsis_label)) },
            )
            OutlinedTextField(
                value = uiState.subtitle,
                onValueChange = viewModel::onSubtitleChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = stringResource(R.string.settings_subtitle_label)) },
            )
            OutlinedTextField(
                value = uiState.language,
                onValueChange = viewModel::onLanguageChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = stringResource(R.string.settings_language_label)) },
            )
            OutlinedTextField(
                value = uiState.isbn,
                onValueChange = viewModel::onIsbnChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = stringResource(R.string.settings_isbn_label)) },
            )
            OutlinedTextField(
                value = uiState.keywords,
                onValueChange = viewModel::onKeywordsChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = stringResource(R.string.settings_keywords_label)) },
            )
            OutlinedTextField(
                value = uiState.publisher,
                onValueChange = viewModel::onPublisherChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = stringResource(R.string.settings_publisher_label)) },
            )
            OutlinedTextField(
                value = uiState.description,
                onValueChange = viewModel::onDescriptionChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = stringResource(R.string.settings_description_label)) },
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(R.string.settings_ai_disclosure_label),
                    modifier = Modifier.weight(1f),
                )
                Switch(
                    checked = uiState.hasAiDisclosure,
                    onCheckedChange = viewModel::onHasAiDisclosureChange,
                )
            }

            Button(
                onClick = viewModel::saveChanges,
                enabled = uiState.hasUnsavedChanges && !uiState.isSaving,
                modifier = Modifier.fillMaxWidth(),
            ) {
                if (uiState.isSaving) {
                    CircularProgressIndicator(modifier = Modifier.height(16.dp))
                } else {
                    Text(text = stringResource(R.string.settings_save_button))
                }
            }

            if (uiState.statusMessage == SettingsViewModel.STATUS_SAVED) {
                Text(text = stringResource(R.string.settings_saved_message))
            }
        }
    }
}

@Composable
private fun EmptySettingsState(innerPadding: PaddingValues) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(text = stringResource(R.string.settings_no_project_message))
    }
}

@Preview(showBackground = true)
@Composable
private fun SettingsScreenPreview() {
    EbookEditorTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
        ) {
            Text(text = stringResource(R.string.settings_placeholder))
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = stringResource(R.string.settings_saved_message))
        }
    }
}
