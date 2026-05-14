package app.iesjdlc.tipslab.data.model

import com.google.firebase.firestore.PropertyName

data class CommentReplyDto(
    val id: String = "",
    val text: String = "",

    @get:PropertyName("author_id")
    @set:PropertyName("author_id")
    var authorId: String = "",

    @get:PropertyName("replied_at")
    @set:PropertyName("replied_at")
    var repliedAt: Long = System.currentTimeMillis()
)
