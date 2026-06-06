package app.iesjdlc.tipslab.domain.usecase.comment

import app.iesjdlc.tipslab.domain.model.CommentWithOwnership
import app.iesjdlc.tipslab.domain.repository.AuthRepository
import app.iesjdlc.tipslab.domain.repository.CommentRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ObserveLifehackCommentsUseCase @Inject constructor(
    private val commentRepository: CommentRepository,
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(lifehackId: String): Flow<List<CommentWithOwnership>> {
        val currentUser = authRepository.getCurrentUser()
        return commentRepository.observeComments(lifehackId)
            .map { comments ->
                comments.map { comment ->
                    CommentWithOwnership(
                        comment = comment,
                        isOwn = comment.author.id == currentUser.id
                    )
                }
            }
    }
}