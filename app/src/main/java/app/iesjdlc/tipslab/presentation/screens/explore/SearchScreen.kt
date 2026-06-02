package app.iesjdlc.tipslab.presentation.screens.explore

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.iesjdlc.tipslab.R
import app.iesjdlc.tipslab.domain.model.Lifehack
import app.iesjdlc.tipslab.presentation.components.LifehackCard
import app.iesjdlc.tipslab.presentation.components.SearchBar

@Composable
fun SearchScreen(
    viewModel: SearchViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onOpenLifehack: (String) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    SearchScreenUI(
        state = uiState,
        onQueryChange = { viewModel.onQueryChange(it) },
        onSearchSubmit = { viewModel.onSearchSubmit(it) },
        onSuggestionClick = { suggestion -> viewModel.onSuggestionClick(suggestion) },
        onHistoryItemClick = { historyItem -> viewModel.onHistoryItemClick(historyItem) },
        onClearHistory = { viewModel.onClearHistory() },
        onOpenLifehack = { lifehack -> viewModel.onLifehackClick(lifehack, onOpenLifehack) },
        onBack = onNavigateBack
    )
}

@Composable
private fun SearchScreenUI(
    state: SearchUiState,
    onQueryChange: (String) -> Unit,
    onSearchSubmit: (String) -> Unit,
    onSuggestionClick: (String) -> Unit,
    onHistoryItemClick: (String) -> Unit,
    onClearHistory: () -> Unit,
    onOpenLifehack: (Lifehack) -> Unit,
    onBack: () -> Unit
) {
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    fun clearSearchBarFocus() {
        focusManager.clearFocus()
    }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp, 24.dp, 24.dp, 0.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SearchBar(
                    modifier = Modifier.weight(1f),
                    query = state.query,
                    onQueryChange = onQueryChange,
                    onSearch = {
                        clearSearchBarFocus()
                        onSearchSubmit(it)
                    },
                    readOnly = false,
                    focusRequester = focusRequester
                )

                Text(
                    text = stringResource(R.string.close),
                    modifier = Modifier.clickable { onBack() },
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                if (state.isLoading) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                } else {
                    when (state.phase) {
                        SearchPhase.IDLE -> HistoryContent(
                            history = state.searchHistory,
                            onItemClick = {
                                clearSearchBarFocus()
                                onHistoryItemClick(it)
                            },
                            onClearHistory = onClearHistory
                        )
                        SearchPhase.SUGGESTING -> SuggestionsContent(
                            suggestions = state.suggestions,
                            history = state.searchHistory,
                            onSuggestionClick = {
                                clearSearchBarFocus()
                                onSuggestionClick(it)
                            },
                            onHistoryItemClick = onHistoryItemClick
                        )
                        SearchPhase.RESULTS -> ResultsContent(
                            results = state.results,
                            onOpenLifehack = onOpenLifehack
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun HistoryContent(
    history: List<String>,
    onItemClick: (String) -> Unit,
    onClearHistory: () -> Unit
) {
    if (history.isEmpty()) {
        EmptyMessage("Sin búsquedas recientes")
        return
    }

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Recientes",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Borrar",
                    modifier = Modifier.clickable { onClearHistory() },
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
        items(history) { item ->
            HistoryItem(text = item, onClick = { onItemClick(item) })
        }
    }
}

@Composable
private fun HistoryItem(
    text: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.History,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(text = text, style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
private fun SuggestionsContent(
    suggestions: List<String>,
    history: List<String>,
    onSuggestionClick: (String) -> Unit,
    onHistoryItemClick: (String) -> Unit
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        if (suggestions.isNotEmpty()) {
            items(suggestions) { suggestion ->
                SuggestionItem(text = suggestion, onClick = { onSuggestionClick(suggestion) })
            }
        }

        if (history.isNotEmpty()) {
            item {
                Text(
                    text = "Recientes",
                    modifier = Modifier.padding(vertical = 8.dp),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            items(history) { item ->
                HistoryItem(text = item, onClick = { onHistoryItemClick(item) })
            }
        }
    }
}

@Composable
private fun SuggestionItem(
    text: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(text = text, style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
private fun ResultsContent(
    results: List<Lifehack>,
    onOpenLifehack: (Lifehack) -> Unit
) {
    if (results.isEmpty()) {
        EmptyMessage("Sin resultados")
        return
    }

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(results) { lifehack ->
            LifehackCard(
                lifehack = lifehack,
                onClick = { onOpenLifehack(lifehack) }
            )
        }
    }
}

@Composable
private fun EmptyMessage(text: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}