package app.iesjdlc.tipslab.data.mapper

import app.iesjdlc.tipslab.data.model.CommentReplyDto
import app.iesjdlc.tipslab.domain.model.CommentReply
import app.iesjdlc.tipslab.domain.model.User
import kotlinx.datetime.Instant
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CommentReplyMapper @Inject constructor() {
    fun toDomain(dto: CommentReplyDto, author: User): CommentReply {
        return CommentReply(
            id = dto.id,
            text = dto.text,
            author = author,
            repliedAt = Instant.fromEpochMilliseconds(dto.repliedAt)
        )
    }

    fun toDto(domain: CommentReply): CommentReplyDto {
        return CommentReplyDto(
            id = domain.id,
            text = domain.text,
            authorId = domain.author.id,
            repliedAt = domain.repliedAt.toEpochMilliseconds()
        )
    }
}