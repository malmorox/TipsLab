package app.iesjdlc.tipslab.presentation.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.iesjdlc.tipslab.R
import app.iesjdlc.tipslab.domain.model.Lifehack
import app.iesjdlc.tipslab.presentation.components.ContentListSection
import app.iesjdlc.tipslab.presentation.components.LifehackSectionListItem
import app.iesjdlc.tipslab.presentation.components.SearchBar

@Composable
fun HomeTab(
    viewModel: HomeViewModel = hiltViewModel(),
    onOpenLifehack: (String) -> Unit,
    onSearch: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    HomeTabUI(
        state = uiState,
        onOpenLifehack = { lifehack -> viewModel.onLifehackClick(lifehack, onOpenLifehack) },
        onSearch = onSearch,
        onRefresh = { viewModel.refresh() }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeTabUI(
    state: HomeUiState,
    onOpenLifehack: (Lifehack) -> Unit,
    onSearch: () -> Unit,
    onRefresh: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp, 24.dp, 24.dp, 0.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SearchBar(
                readOnly = true,
                onClick = onSearch
            )

            PullToRefreshBox(
                isRefreshing = state.isRefreshing,
                onRefresh = onRefresh,
                modifier = Modifier.fillMaxSize()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    ContentListSection(
                        title = stringResource(R.string.for_you),
                        sectionState = state.sections.forYou,
                        emptyMessage = stringResource(R.string.no_popular_category_lifehacks),
                    ) { lifehack ->
                        LifehackSectionListItem(
                            lifehack = lifehack,
                            onClick = { onOpenLifehack(lifehack) }
                        )
                    }

                    ContentListSection(
                        title = stringResource(R.string.trending),
                        sectionState = state.sections.trending,
                        emptyMessage = stringResource(R.string.no_popular_category_lifehacks),
                    ) { lifehack ->
                        LifehackSectionListItem(
                            lifehack = lifehack,
                            onClick = { onOpenLifehack(lifehack) }
                        )
                    }

                    ContentListSection(
                        title = stringResource(R.string.recent),
                        sectionState = state.sections.recent,
                        emptyMessage = stringResource(R.string.no_recent_category_lifehacks),
                    ) { lifehack ->
                        LifehackSectionListItem(
                            lifehack = lifehack,
                            onClick = { onOpenLifehack(lifehack) }
                        )
                    }
                }
            }
        }
    }
}