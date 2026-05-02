package app.iesjdlc.tipslab.domain.model

import kotlinx.datetime.Instant

data class Lifehack(
    val id: String,
    val title: String,
    val description: String,
    val steps: List<String>,
    val category: Category,
    val author: User,
    val media: Media?,
    val createdAt: Instant,
    val updatedAt: Instant,
    val likesCount: Int
)