package app.iesjdlc.tipslab.presentation.screens.category

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.More
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.iesjdlc.tipslab.R
import app.iesjdlc.tipslab.domain.model.Lifehack
import app.iesjdlc.tipslab.presentation.components.ConfirmOrDismissDialog
import app.iesjdlc.tipslab.presentation.components.ContentListSection
import app.iesjdlc.tipslab.presentation.components.LifehackStepsList
import app.iesjdlc.tipslab.presentation.components.OptionsContextMenu

@Composable
fun CategoryScreen(
    viewModel: CategoryViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onOpenLifehack: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    CategoryScreenUI(
        state = uiState,
        onOpenLifehack = { lifehack -> viewModel.onLifehackClick(lifehack, onOpenLifehack) },
        onBack = onNavigateBack,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CategoryScreenUI(
    state: CategoryUiState,
    onOpenLifehack: (Lifehack) -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    if (state.category != null) {
                        Text(
                            text = state.category.name
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBack
                    ) {
                        Icon(
                            modifier = Modifier.size(24.dp),
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = stringResource(R.string.go_back),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            state.category?.let { category ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    ContentListSection(
                        title = stringResource(R.string.recent),
                        //sectionState = state.sections.popular,
                        onLifehackClick = onOpenLifehack
                    )

                    ContentListSection(
                        title = stringResource(R.string.popular),
                        //sectionState = state.sections.popular,
                        onLifehackClick = onOpenLifehack
                    )
                }
            }
        }
    }
}