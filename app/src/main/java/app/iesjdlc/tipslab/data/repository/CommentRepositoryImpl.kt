package app.iesjdlc.tipslab.data.repository

import app.iesjdlc.tipslab.domain.model.Comment
import app.iesjdlc.tipslab.domain.model.CommentReply
import app.iesjdlc.tipslab.domain.repository.CommentRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CommentRepositoryImpl @Inject constructor(

) : CommentRepository {
    override fun observeComments(lifehackId: String): Flow<List<Comment>> {
        TODO("Not yet implemented")
    }

    override suspend fun addComment(
        lifehackId: String,
        comment: Comment
    ): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun addReply(
        lifehackId: String,
        commentId: String,
        reply: CommentReply
    ): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteComment(
        lifehackId: String,
        commentId: String
    ): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteReply(
        lifehackId: String,
        commentId: String,
        replyId: String
    ): Result<Unit> {
        TODO("Not yet implemented")
    }
}