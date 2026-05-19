package app.iesjdlc.tipslab.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Send
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.iesjdlc.tipslab.domain.model.Comment
import app.iesjdlc.tipslab.presentation.common.CommentInputMode

@Composable
fun CommentsSection(
    commentsCount: Int,
    comments: List<Comment>,
    showComments: Boolean,
    onShowComments: () -> Unit,
    commentText: String,
    onCommentTextChange: (String) -> Unit,
    inputMode: CommentInputMode,
    isAuthor: Boolean,
    onSendComment: () -> Unit,
    onReplyTo: (String) -> Unit,
    onCancelReply: () -> Unit,
    onDeleteComment: (String) -> Unit,
    onDeleteReply: (String, String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {

        // Header clickable
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onShowComments() },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Comentarios ($commentsCount)",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface
            )
            Icon(
                imageVector = if (showComments) Icons.Rounded.KeyboardArrowUp else Icons.Rounded.KeyboardArrowDown,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        if (showComments) {

            // Input mode label + cancelar
            when (inputMode) {
                is CommentInputMode.Reply -> {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Respondiendo a un comentario",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        TextButton(onClick = onCancelReply) {
                            Text("Cancelar")
                        }
                    }
                }
                is CommentInputMode.NewComment -> {
                    if (commentText.isNotBlank()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            TextButton(onClick = onCancelReply) {
                                Text("Cancelar")
                            }
                        }
                    }
                }
            }

            // Input field
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = commentText,
                    onValueChange = onCommentTextChange,
                    placeholder = {
                        Text(
                            when (inputMode) {
                                is CommentInputMode.Reply -> "Escribe una respuesta..."
                                is CommentInputMode.NewComment -> "Escribe un comentario..."
                            }
                        )
                    },
                    modifier = Modifier.weight(1f),
                    shape = MaterialTheme.shapes.medium,
                    maxLines = 3
                )
                IconButton(
                    onClick = onSendComment,
                    enabled = commentText.isNotBlank()
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.Send,
                        contentDescription = "Enviar",
                        tint = if (commentText.isNotBlank())
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Lista de comentarios
            if (comments.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No hay comentarios aún",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                comments.forEach { comment ->
                    CommentItem(
                        comment = comment,
                        isAuthor = isAuthor,
                        onReplyTo = { onReplyTo(comment.id) },
                        onDelete = { onDeleteComment(comment.id) },
                        onDeleteReply = { replyId -> onDeleteReply(comment.id, replyId) }
                    )
                }
            }
        }
    }
}

@Composable
private fun CommentItem(
    comment: Comment,
    isAuthor: Boolean,
    onReplyTo: () -> Unit,
    onDelete: () -> Unit,
    onDeleteReply: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                UserAvatarImage(user = comment.author, size = 28.dp)
                Column {
                    Text(
                        text = "@${comment.author.username}",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = comment.text,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            if (isAuthor) {
                IconButton(onClick = onDelete, modifier = Modifier.size(20.dp)) {
                    Icon(
                        imageVector = Icons.Rounded.Delete,
                        contentDescription = "Eliminar",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        Row(
            modifier = Modifier.padding(start = 36.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = onReplyTo) {
                Text("Responder", style = MaterialTheme.typography.labelSmall)
            }
            if (comment.repliesCount > 0) {
                TextButton(onClick = { /* onExpandReplies */ }) {
                    Text(
                        "Ver ${comment.repliesCount} respuestas",
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
        }
    }
}