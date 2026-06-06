package app.iesjdlc.tipslab.data.repository

import app.iesjdlc.tipslab.data.datasource.CommentDataSource
import app.iesjdlc.tipslab.data.mapper.CommentMapper
import app.iesjdlc.tipslab.data.resolver.CommentResolver
import app.iesjdlc.tipslab.domain.model.Comment
import app.iesjdlc.tipslab.domain.repository.CommentRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CommentRepositoryImpl @Inject constructor(
    private val dataSource: CommentDataSource,
    private val commentMapper: CommentMapper,
    private val resolver: CommentResolver
) : CommentRepository {
    override fun observeComments(lifehackId: String): Flow<List<Comment>> =
        dataSource.observeByLifehack(lifehackId)
            .map { dtos -> resolver.resolve(dtos) }

    override suspend fun addComment(
        lifehackId: String,
        comment: Comment
    ): Result<Unit> = runCatching {
        dataSource.add(
            lifehackId,
            commentMapper.toDto(comment)
        )
    }

    override suspend fun deleteComment(
        lifehackId: String,
        commentId: String
    ): Result<Unit> = runCatching {
        dataSource.delete(
            lifehackId,
            commentId
        )
    }
}