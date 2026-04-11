package app.iesjdlc.tipslab.domain.model

data class Lifehack(
    val id: String,
    val title: String,
    val description: String,
    val category: Category,
    val author: User,
    val media: Media?,
    val likedCount: Int,
)

data class Media(
    val type: MediaType,
    val url: String,
)

enum class MediaType {
    IMAGE,
    VIDEO
}