package app.iesjdlc.tipslab.presentation.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.iesjdlc.tipslab.R
import app.iesjdlc.tipslab.presentation.components.ContentListSection
import app.iesjdlc.tipslab.presentation.components.SearchBar

@Composable
fun HomeTab(
    viewModel: HomeViewModel = hiltViewModel(),
    onOpenLifehack: (String) -> Unit,
    onOpenCategory: (Int) -> Unit,
    onSearch: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    HomeTabUI(
        state = uiState,
        onOpenLifehack = onOpenLifehack,
        onOpenCategory = onOpenCategory,
        onSearch = onSearch
    )
}

@Composable
private fun HomeTabUI(
    state: HomeUiState,
    onOpenLifehack: (String) -> Unit,
    onOpenCategory: (Int) -> Unit,
    onSearch: () -> Unit
) {
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
            SearchBar(
                readOnly = true,
                onClick = onSearch
            )

            Column(

            ) {
                ContentListSection(
                    title = stringResource(R.string.recent),
                ) {

                }

                ContentListSection(
                    title = stringResource(R.string.popular),
                ) {

                }
            }
        }
    }
}