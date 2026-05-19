package app.iesjdlc.tipslab.data.model

import com.google.firebase.firestore.PropertyName

data class LifehackDto(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val steps: List<String> = emptyList(),

    @get:PropertyName("category_id")
    @set:PropertyName("category_id")
    var categoryId: Int = 0,

    @get:PropertyName("author_id")
    @set:PropertyName("author_id")
    var authorId: String = "",

    @get:PropertyName("media_type")
    @set:PropertyName("media_type")
    var mediaType: String? = null,

    @get:PropertyName("media_url")
    @set:PropertyName("media_url")
    var mediaUrl: String? = null,

    @get:PropertyName("created_at")
    @set:PropertyName("created_at")
    var createdAt: Long = System.currentTimeMillis(),

    @get:PropertyName("updated_at")
    @set:PropertyName("updated_at")
    var updatedAt: Long = System.currentTimeMillis(),

    @get:PropertyName("likes_count")
    @set:PropertyName("likes_count")
    var likesCount: Int = 0,

    @get:PropertyName("comments_count")
    @set:PropertyName("comments_count")
    var commentsCount: Int = 0
)