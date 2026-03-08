package app.iesjdlc.tipslab.models.domain

data class Lifehack(
    val id: String,
    val title: String,
    val description: String,
    val category: Category,
    val author: User,
    val mediaType: MediaType,
    val mediaUrl: String?,
)

enum class MediaType {
    IMAGE,
    VIDEO,
    YOUTUBE,
    TIKTOK,
    INSTAGRAM,
    NONE
}