package app.iesjdlc.tipslab.data.datasource.remote

import app.iesjdlc.tipslab.data.model.CommentDto
import app.iesjdlc.tipslab.data.model.CommentReplyDto
import kotlinx.coroutines.flow.Flow

interface CommentDataSource {
    fun observeByLifehack(lifehackId: String): Flow<List<CommentDto>>
    suspend fun add(
        lifehackId: String,
        comment: CommentDto
    ): Result<Unit>
    suspend fun addReply(
        lifehackId: String,
        commentId: String,
        reply: CommentReplyDto
    ): Result<Unit>
    suspend fun delete(
        lifehackId: String,
        commentId: String
    ): Result<Unit>
    suspend fun deleteReply(
        lifehackId: String,
        commentId: String,
        replyId: String
    ): Result<Unit>
}