package app.iesjdlc.tipslab.domain.model

data class Media(
    val type: MediaType,
    val url: String,
)

enum class MediaType {
    IMAGE,
    VIDEO
}