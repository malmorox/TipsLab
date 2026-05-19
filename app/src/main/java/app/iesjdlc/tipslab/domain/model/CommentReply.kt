package app.iesjdlc.tipslab.domain.model

import kotlinx.datetime.Instant

data class CommentReply(
    val id: String,
    val text: String,
    val author: User,
    val repliedAt: Instant
)