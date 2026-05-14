package app.iesjdlc.tipslab.data.mapper

import app.iesjdlc.tipslab.data.model.CommentDto
import app.iesjdlc.tipslab.domain.model.Comment
import app.iesjdlc.tipslab.domain.model.CommentReply
import app.iesjdlc.tipslab.domain.model.User
import kotlinx.datetime.Instant
import javax.inject.Inject

class CommentMapper @Inject constructor(
    private val replyMapper: CommentReplyMapper
) {
    fun toDomain(dto: CommentDto, author: User, replies: List<CommentReply>): Comment {
        return Comment(
            id = dto.id,
            text = dto.text,
            author = author,
            commentedAt = Instant.fromEpochMilliseconds(dto.commentedAt),
            likesCount = dto.likesCount,
            replies = replies
        )
    }

    fun toDto(domain: Comment): CommentDto {
        return CommentDto(
            id = domain.id,
            text = domain.text,
            authorId = domain.author.id,
            commentedAt = domain.commentedAt.toEpochMilliseconds(),
            likesCount = domain.likesCount,
            replies = domain.replies.map { replyMapper.toDto(it) }
        )
    }
}