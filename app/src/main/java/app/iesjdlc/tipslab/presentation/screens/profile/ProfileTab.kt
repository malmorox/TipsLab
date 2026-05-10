package app.iesjdlc.tipslab.presentation.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.GridOn
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import app.iesjdlc.tipslab.R
import coil3.compose.AsyncImage

@Composable
fun ProfileTab(
    navController: NavController,
    viewModel: ProfileViewModel = hiltViewModel(),
    onEditProfile: () -> Unit,
    onLogout: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val refreshState = navController
        .currentBackStackEntry
        ?.savedStateHandle
        ?.getStateFlow("refresh_profile", false)
        ?.collectAsStateWithLifecycle()

    LaunchedEffect(refreshState?.value) {
        if (refreshState?.value == true) {
            viewModel.loadUser()

            navController.currentBackStackEntry
                ?.savedStateHandle
                ?.set("refresh_profile", false)
        }
    }

    ProfileTabUI(
        state = uiState,
        onEditProfile = onEditProfile,
        onLogout = { viewModel.onLogout(onLogout) }
    )
}

@Composable
private fun ProfileTabUI(
    state: ProfileUiState,
    onEditProfile: () -> Unit,
    onLogout: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = stringResource(R.string.my_profile),
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.weight(1f)
            )

            IconButton(
                onClick = onLogout
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Logout,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            AsyncImage(
                model = state.photoUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(108.dp)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "@${state.username}",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(18.dp))

            Button(
                onClick = onEditProfile,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier
                    .height(46.dp)
            ) {
                Text(
                    text = stringResource(R.string.edit_profile),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }

        HorizontalDivider()

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {

            Icon(
                imageVector = Icons.Default.GridOn,
                contentDescription = null
            )

            Icon(
                imageVector = Icons.Default.FavoriteBorder,
                contentDescription = null
            )
        }

        HorizontalDivider()

        if (state.posts.isEmpty()) {

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.no_posts),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

        } else {

            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier.fillMaxSize()
            ) {

//                items(state.posts) { post ->
//
//                    AsyncImage(
//                        model = post,
//                        contentDescription = null,
//                        contentScale = ContentScale.Crop,
//                        modifier = Modifier
//                            .aspectRatio(1f)
//                            .padding(2.dp)
//                    )
//                }
            }
        }
    }
}