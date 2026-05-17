package app.iesjdlc.tipslab.data.datasource

import app.iesjdlc.tipslab.data.model.CommentDto
import app.iesjdlc.tipslab.data.model.CommentReplyDto
import kotlinx.coroutines.flow.Flow

interface CommentDataSource {
    fun observeByLifehack(lifehackId: String): Flow<List<CommentDto>>
    suspend fun add(
        lifehackId: String,
        dto: CommentDto
    )
    suspend fun addReply(
        lifehackId: String,
        commentId: String,
        dto: CommentReplyDto
    )
    suspend fun delete(
        lifehackId: String,
        commentId: String
    )
    suspend fun deleteReply(
        lifehackId: String,
        commentId: String,
        replyId: String
    )
}