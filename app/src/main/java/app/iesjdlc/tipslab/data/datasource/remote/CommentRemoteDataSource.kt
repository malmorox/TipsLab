package app.iesjdlc.tipslab.data.datasource.remote

import app.iesjdlc.tipslab.data.model.CommentDto
import app.iesjdlc.tipslab.data.model.CommentReplyDto
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class CommentRemoteDataSource @Inject constructor(
    private val db: FirebaseFirestore
) : CommentDataSource {
    override fun observeByLifehack(lifehackId: String): Flow<List<CommentDto>> = callbackFlow {
        val subscription = db.collection("lifehacks")
            .document(lifehackId)
            .collection("comments")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                snapshot?.documents?.mapNotNull { it.toObject(CommentDto::class.java) }?.let { trySend(it) }
            }
        awaitClose { subscription.remove() }
    }

    override suspend fun add(
        lifehackId: String,
        comment: CommentDto
    ): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun addReply(
        lifehackId: String,
        commentId: String,
        reply: CommentReplyDto
    ): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun delete(
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