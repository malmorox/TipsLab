package app.iesjdlc.tipslab.data.datasource

import app.iesjdlc.tipslab.data.model.CommentDto
import kotlinx.coroutines.flow.Flow

interface CommentDataSource {
    fun observeByLifehack(lifehackId: String): Flow<List<CommentDto>>
    suspend fun add(
        lifehackId: String,
        dto: CommentDto
    )
    suspend fun delete(
        lifehackId: String,
        commentId: String
    )
}