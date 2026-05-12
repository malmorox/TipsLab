package app.iesjdlc.tipslab.domain.model

import kotlinx.datetime.Instant

data class Reply(
    val id: String,
    val text: String,
    val author: User,
    val createdAt: Instant
)