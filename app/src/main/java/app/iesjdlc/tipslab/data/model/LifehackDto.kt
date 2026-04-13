package app.iesjdlc.tipslab.data.model

import com.google.firebase.firestore.PropertyName

data class LifehackDto(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val steps: List<String> = emptyList(),

    @PropertyName("category_id")
    val categoryId: String = "",

    @PropertyName("author_id")
    val authorId: String = "",

    @PropertyName("media_type")
    val mediaType: String? = null,

    @PropertyName("media_url")
    val mediaUrl: String? = null,

    @PropertyName("created_at")
    val createdAt: Long = System.currentTimeMillis(),

    @PropertyName("updated_at")
    val updatedAt: Long = System.currentTimeMillis(),

    @PropertyName("likes_count")
    val likesCount: Int = 0
)