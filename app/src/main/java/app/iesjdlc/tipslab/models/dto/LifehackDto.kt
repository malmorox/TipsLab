package app.iesjdlc.tipslab.models.dto

data class LifehackDto(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val category_id: String = "",
    val media_type: String = "NONE",
    val media_url: String? = null,
)