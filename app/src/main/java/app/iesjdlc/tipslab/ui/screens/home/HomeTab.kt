package app.iesjdlc.tipslab.ui.screens.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import app.iesjdlc.tipslab.R
import app.iesjdlc.tipslab.ui.components.CategoryChipsRow

@Composable
fun HomeTab(
    viewModel: HomeViewModel = hiltViewModel(),
    onLifehackClick: (String) -> Unit,
    onOpenCategory: (Int) -> Unit,
    onOpenSearch: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    HomeTabUI(
        state = uiState,
        onLifehackClick = onLifehackClick,
        onOpenCategory = onOpenCategory,
        onSearch = onOpenSearch
    )
}

@Composable
private fun HomeTabUI(
    state: HomeUiState,
    onLifehackClick: (String) -> Unit,
    onOpenCategory: (Int) -> Unit,
    onSearch: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 24.dp, vertical = 36.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.create_lifehack),
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Start
                )

                IconButton(
                    onClick = onSearch
                ) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        imageVector = Icons.Rounded.Close,
                        contentDescription = stringResource(R.string.close),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            ContentSection(label = stringResource(R.string.categories)) {
                //TODO meter aqui lifehacks
            }

            Spacer(modifier = Modifier.height(16.dp))

            ContentSection(label = stringResource(R.string.categories)) {
                //TODO meter aqui lifehacks
            }

            Spacer(modifier = Modifier.height(16.dp))

            ContentSection(label = stringResource(R.string.categories)) {
                //TODO meter aqui lifehacks
            }
        }
    }
}

@Composable
private fun ContentSection(
    label: String,
    content: @Composable () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        content()
    }
}

