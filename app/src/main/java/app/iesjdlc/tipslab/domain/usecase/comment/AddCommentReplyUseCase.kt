package app.iesjdlc.tipslab.domain.usecase.comment

import app.iesjdlc.tipslab.domain.model.CommentReply
import app.iesjdlc.tipslab.domain.repository.AuthRepository
import app.iesjdlc.tipslab.domain.repository.CommentRepository
import kotlinx.datetime.Clock
import javax.inject.Inject

class AddCommentReplyUseCase @Inject constructor(
    private val commentRepository: CommentRepository,
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(lifehackId: String, commentId: String, text: String): Result<Unit> {
        if (text.isBlank()) return Result.failure(IllegalArgumentException("Respuesta vacía"))
        val currentUser = authRepository.getCurrentUser()
        val reply = CommentReply(
            id = "",
            text = text,
            author = currentUser,
            repliedAt = Clock.System.now()
        )
        return commentRepository.addReply(lifehackId, commentId, reply)
    }
}