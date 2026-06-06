package app.iesjdlc.tipslab.presentation.screens.lifehack.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Bookmark
import androidx.compose.material.icons.rounded.BookmarkBorder
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.iesjdlc.tipslab.R
import app.iesjdlc.tipslab.domain.model.Category
import app.iesjdlc.tipslab.domain.model.User
import app.iesjdlc.tipslab.presentation.common.UploadState
import app.iesjdlc.tipslab.presentation.components.AnimatedCircleButton
import app.iesjdlc.tipslab.presentation.components.CommentsSection
import app.iesjdlc.tipslab.presentation.components.ConfirmOrDismissDialog
import app.iesjdlc.tipslab.presentation.components.LifehackStepsList
import app.iesjdlc.tipslab.presentation.components.MediaViewer
import app.iesjdlc.tipslab.presentation.components.OptionsContextMenu
import app.iesjdlc.tipslab.presentation.components.UserAvatarImage

@Composable
fun LifehackDetailScreen(
    viewModel: LifehackDetailViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onEditLifehack: (String) -> Unit,
    onDeleteLifehack: () -> Unit,
    onOpenCategory: (Int) -> Unit
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    LifehackDetailScreenUI(
        state = uiState.value,
        onOptions = { viewModel.onOptionsClick() },
        onDismissOptions = { viewModel.onDismissOptionsMenu() },
        onEdit = { viewModel.onEditClick(onEditLifehack) },
        onDelete = { viewModel.onDeleteClick() },
        onConfirmDelete = { viewModel.onConfirmDelete(onDeleteLifehack) },
        onDismissDelete = { viewModel.onDismissDelete() },
        onOpenCategory = { viewModel.onCategoryClick(onOpenCategory) },
        onLike = { viewModel.onLikeClick() },
        onSave = { viewModel.onSaveClick() },
        onShowComments = { viewModel.onShowComments() },
        onCommentTextChange = { viewModel.onCommentTextChange(it) },
        onSendComment = { viewModel.onSendComment() },
        onDeleteComment = { viewModel.onDeleteComment(it) },
        onBack = onNavigateBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LifehackDetailScreenUI(
    state: LifehackDetailUiState,
    onOptions: () -> Unit,
    onDismissOptions: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onConfirmDelete: () -> Unit,
    onDismissDelete: () -> Unit,
    onOpenCategory: (Category) -> Unit,
    onLike: () -> Unit,
    onSave: () -> Unit,
    onShowComments: () -> Unit,
    onCommentTextChange: (String) -> Unit,
    onSendComment: () -> Unit,
    onDeleteComment: (String) -> Unit,
    onBack: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    if (state.lifehack != null) {
                        Text(
                            text = state.lifehack.title,
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.primary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
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
                },
                actions = {
                    if (state.isAuthor) {
                        IconButton(
                            onClick = onOptions
                        ) {
                            Icon(
                                modifier = Modifier.size(24.dp),
                                imageVector = Icons.Rounded.MoreVert,
                                contentDescription = stringResource(R.string.options),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
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
            if (state.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else if (state.errorMessage != null) {
                Text(
                    text = state.errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            state.lifehack?.let { lifehack ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    when {
                        state.uploadState == UploadState.Loading -> {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(16f / 9f),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                        state.uploadState == UploadState.Error -> {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(16f / 9f),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Error al subir el media",
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                        lifehack.media != null -> {
                            MediaViewer(
                                mediaUrl = lifehack.media.url,
                                mediaType = lifehack.media.type
                            )
                        }
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(
                                start = 24.dp,
                                end = 24.dp,
                                top = if (lifehack.media == null && state.uploadState == null) 24.dp else 0.dp,
                                bottom = 24.dp
                            ),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Text(
                                text = lifehack.title,
                                style = MaterialTheme.typography.headlineLarge,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.weight(1f)
                            )

                            if (!state.isAuthor) {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    LikeButton(
                                        isLiked = state.isLiked,
                                        onClick = onLike
                                    )
                                    SaveButton(
                                        isSaved = state.isSaved,
                                        onClick = onSave
                                    )
                                }
                            }
                        }

                        SectionWithHeading(heading = stringResource(R.string.description)) {
                            Text(
                                text = lifehack.description,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                            )
                        }

                        if (lifehack.steps.isNotEmpty()) {
                            SectionWithHeading(heading = stringResource(R.string.steps)) {
                                LifehackStepsList(steps = lifehack.steps)
                            }
                        }

                        SectionWithHeading(heading = stringResource(R.string.category)) {
                            Text(
                                text = lifehack.category.name,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.clickable {
                                    onOpenCategory(lifehack.category)
                                }
                            )
                        }

                        if (!state.isAuthor) {
                            AuthorSection(author = lifehack.author)
                        }

                        CommentsSection(
                            commentsCount = lifehack.commentsCount,
                            comments = state.comments,
                            showComments = state.showComments,
                            onShowComments = onShowComments,
                            commentText = state.commentText,
                            onCommentTextChange = onCommentTextChange,
                            onSendComment = onSendComment,
                            onDeleteComment = onDeleteComment
                        )
                    }
                }
            }
        }
    }

    if (state.showOptionsContextMenu) {
        OptionsContextMenu(
            onDismiss = onDismissOptions,
            onEdit = onEdit,
            onDelete = onDelete
        )
    }

    if (state.showConfirmDeleteDialog) {
        ConfirmOrDismissDialog(
            onConfirm = onConfirmDelete,
            onDismiss = onDismissDelete,
            title = stringResource(R.string.delete_lifehack),
            message = stringResource(R.string.delete_lifehack_message),
            confirmText = stringResource(R.string.delete),
            dismissText = stringResource(R.string.cancel)
        )
    }
}

@Composable
private fun SectionWithHeading(
    heading: String,
    content: @Composable () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(
            text = heading,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        content()
    }
}

@Composable
private fun AuthorSection(author: User) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.published_by),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(3.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            UserAvatarImage(
                user = author,
                size = 24.dp
            )

            Text(
                text = "@${author.username}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun LikeButton(
    isLiked: Boolean,
    onClick: () -> Unit
) {
    AnimatedCircleButton(
        isActive = isLiked,
        onClick = onClick,
        activeIcon = Icons.Rounded.Favorite,
        inactiveIcon = Icons.Rounded.FavoriteBorder,
        contentDescription = if (isLiked) stringResource(R.string.unlike) else stringResource(R.string.like)
    )
}

@Composable
private fun SaveButton(
    isSaved: Boolean,
    onClick: () -> Unit
) {
    AnimatedCircleButton(
        isActive = isSaved,
        onClick = onClick,
        activeIcon = Icons.Rounded.Bookmark,
        inactiveIcon = Icons.Rounded.BookmarkBorder,
        contentDescription = if (isSaved) stringResource(R.string.unsave) else stringResource(R.string.save)
    )
}