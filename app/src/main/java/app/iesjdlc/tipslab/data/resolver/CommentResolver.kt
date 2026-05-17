package app.iesjdlc.tipslab.data.resolver

import app.iesjdlc.tipslab.data.datasource.UserDataSource
import app.iesjdlc.tipslab.data.mapper.CommentMapper
import app.iesjdlc.tipslab.data.mapper.CommentReplyMapper
import app.iesjdlc.tipslab.data.mapper.UserMapper
import app.iesjdlc.tipslab.data.model.CommentDto
import app.iesjdlc.tipslab.domain.model.Comment
import app.iesjdlc.tipslab.domain.model.User
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CommentResolver @Inject constructor(
    private val userDataSource: UserDataSource,
    private val commentMapper: CommentMapper,
    private val replyMapper: CommentReplyMapper,
    private val userMapper: UserMapper
) {
    suspend fun resolve(dtos: List<CommentDto>): List<Comment> {
        if (dtos.isEmpty()) return emptyList()

        // Recoge todos los authorIds de comentarios y replies
        val commentAuthorIds = dtos.map { it.authorId }
        val replyAuthorIds = dtos.flatMap { it.replies.map { r -> r.authorId } }
        val allAuthorIds = (commentAuthorIds + replyAuthorIds).distinct()

        val usersById = fetchUsers(allAuthorIds)

        return dtos.mapNotNull { dto -> enrich(dto, usersById) }
    }

    suspend fun resolveOne(dto: CommentDto): Comment? {
        val replyAuthorIds = dto.replies.map { it.authorId }.distinct()
        val allAuthorIds = (listOf(dto.authorId) + replyAuthorIds).distinct()
        val usersById = fetchUsers(allAuthorIds)
        return enrich(dto, usersById)
    }

    private suspend fun fetchUsers(ids: List<String>): Map<String, User> =
        userDataSource.getByIds(ids)
            .mapValues { (_, dto) -> userMapper.toDomain(dto) }

    private fun enrich(
        dto: CommentDto,
        usersById: Map<String, User>
    ): Comment? {
        val author = usersById[dto.authorId] ?: return null
        val replies = dto.replies.mapNotNull { replyDto ->
            val replyAuthor = usersById[replyDto.authorId] ?: return@mapNotNull null
            replyMapper.toDomain(replyDto, replyAuthor)
        }
        return commentMapper.toDomain(dto, author, replies)
    }
}