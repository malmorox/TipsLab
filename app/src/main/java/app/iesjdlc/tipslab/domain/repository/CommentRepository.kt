package app.iesjdlc.tipslab.domain.repository

import app.iesjdlc.tipslab.domain.model.Comment
import kotlinx.coroutines.flow.Flow

interface CommentRepository {
    fun observeComments(lifehackId: String): Flow<List<Comment>>
    suspend fun addComment(lifehackId: String, text: String): Result<Unit>
    suspend fun addReply(lifehackId: String, commentId: String, text: String): Result<Unit>
    suspend fun deleteComment(lifehackId: String, commentId: String): Result<Unit>
    suspend fun deleteReply(lifehackId: String, commentId: String, replyId: String): Result<Unit>
}