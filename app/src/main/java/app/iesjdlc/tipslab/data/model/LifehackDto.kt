package app.iesjdlc.tipslab.data.model

data class LifehackDto(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val category_id: String = "",
    val author_id: String = "",
    val media_type: String? = null,
    val media_url: String? = null,
    val saved_count: Int = 0,
)