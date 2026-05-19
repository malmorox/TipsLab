package app.iesjdlc.tipslab.domain.usecase.comment

import app.iesjdlc.tipslab.domain.model.Comment
import app.iesjdlc.tipslab.domain.repository.AuthRepository
import app.iesjdlc.tipslab.domain.repository.CommentRepository
import kotlinx.datetime.Clock
import javax.inject.Inject

class AddCommentUseCase @Inject constructor(
    private val commentRepository: CommentRepository,
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(lifehackId: String, text: String): Result<Unit> {
        if (text.isBlank()) return Result.failure(IllegalArgumentException("Comentario vacío"))
        val currentUser = authRepository.getCurrentUser()
        val comment = Comment(
            id = "",
            text = text,
            author = currentUser,
            commentedAt = Clock.System.now(),
            likesCount = 0,
            repliesCount = 0
        )
        return commentRepository.addComment(lifehackId, comment)
    }
}