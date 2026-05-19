package app.iesjdlc.tipslab.domain.model

import kotlinx.datetime.Instant

data class Comment(
    val id: String,
    val text: String,
    val author: User,
    val commentedAt: Instant,
    val likesCount: Int,
    val repliesCount: Int
)
