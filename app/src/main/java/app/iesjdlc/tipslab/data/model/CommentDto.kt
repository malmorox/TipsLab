package app.iesjdlc.tipslab.data.model

import com.google.firebase.firestore.PropertyName

data class CommentDto(
    val id: String = "",
    val text: String = "",

    @get:PropertyName("author_id")
    @set:PropertyName("author_id")
    var authorId: String = "",

    @get:PropertyName("commented_at")
    @set:PropertyName("commented_at")
    var commentedAt: Long = System.currentTimeMillis(),

    @get:PropertyName("likes_count")
    @set:PropertyName("likes_count")
    var likesCount: Int = 0,

    @get:PropertyName("replies_count")
    @set:PropertyName("replies_count")
    var repliesCount: Int = 0
)