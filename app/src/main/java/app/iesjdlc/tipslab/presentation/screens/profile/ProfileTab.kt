package app.iesjdlc.tipslab.presentation.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.GridOn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import app.iesjdlc.tipslab.R
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.rounded.BookmarkBorder
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.GridOn
import androidx.compose.ui.graphics.vector.ImageVector
import app.iesjdlc.tipslab.domain.model.Lifehack
import app.iesjdlc.tipslab.presentation.components.ProfilePostsList
import app.iesjdlc.tipslab.presentation.components.UserAvatarImage

@Composable
fun ProfileTab(
    viewModel: ProfileViewModel = hiltViewModel(),
    onEditProfile: () -> Unit,
    onOpenLifehack: (String) -> Unit,
    onLogout: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ProfileTabUI(
        state = uiState,
        onEditProfile = onEditProfile,
        onLogout = { viewModel.onLogout(onLogout) },
        onOpenLifehack = onOpenLifehack
    )
}

private data class ProfilePage(
    val icon: ImageVector,
    val label: Int,
    val emptyText: Int
)

@Composable
private fun ProfileTabUI(
    state: ProfileUiState,
    onEditProfile: () -> Unit,
    onOpenLifehack: (String) -> Unit,
    onLogout: () -> Unit
) {
    val pages = listOf(
        ProfilePage(Icons.Rounded.GridOn, R.string.my_posts, R.string.no_posts),
        ProfilePage(Icons.Rounded.FavoriteBorder, R.string.my_favorites, R.string.no_liked_posts),
        ProfilePage(Icons.Rounded.BookmarkBorder, R.string.my_saved, R.string.no_saved_posts)
    )

    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { pages.size }
    )

    val scope = rememberCoroutineScope()

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

            IconButton(onClick = onLogout) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Logout,
                    contentDescription = stringResource(R.string.log_out),
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }

        state.user?.let { user ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                UserAvatarImage(
                    user = user,
                    size = 108.dp
                )

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "@${user.username}",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(18.dp))

                Button(
                    onClick = onEditProfile,
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier.height(46.dp)
                ) {
                    Text(
                        text = stringResource(R.string.edit_profile),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            HorizontalDivider()

            TabRow(
                selectedTabIndex = pagerState.currentPage,
                containerColor = MaterialTheme.colorScheme.background,
                indicator = { tabPositions ->
                    TabRowDefaults.Indicator(
                        modifier = Modifier.tabIndicatorOffset(
                            tabPositions[pagerState.currentPage]
                        ),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            ) {
                pages.forEachIndexed { index, page ->
                    Tab(
                        selected = pagerState.currentPage == index,
                        onClick = {
                            scope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        },
                        selectedContentColor = MaterialTheme.colorScheme.primary,
                        unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        icon = {
                            Icon(
                                imageVector = page.icon,
                                contentDescription = stringResource(page.label)
                            )
                        }
                    )
                }
            }

            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                val currentPage = pages[page]

                ProfilePostsList(
                    posts = when (page) {
                        0 -> state.posts
                        1 -> state.favoritePosts
                        else -> state.savedPosts
                    },
                    emptyText = stringResource(currentPage.emptyText),
                    onOpenLifehack = onOpenLifehack
                )
            }
        }
    }
}