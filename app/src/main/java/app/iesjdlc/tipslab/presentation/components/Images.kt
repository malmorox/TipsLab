package app.iesjdlc.tipslab.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.core.net.toUri
import app.iesjdlc.tipslab.domain.model.User
import coil3.compose.AsyncImage

@Composable
fun UserAvatarImage(
    user: User,
    size: Dp,
    modifier: Modifier = Modifier
) {
    val imageModel = remember(user.photoUrl) {
        user.photoUrl?.let { "${it}?t=${System.currentTimeMillis()}" }?.toUri()
    }

    AsyncImage(
        model = imageModel,
        contentDescription = "@${user.username}",
        contentScale = ContentScale.Crop,
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surfaceVariant)
    )
}