package app.iesjdlc.tipslab.data.datasource.remote

import app.iesjdlc.tipslab.core.constants.DBConstants
import app.iesjdlc.tipslab.data.model.CommentDto
import app.iesjdlc.tipslab.data.model.CommentReplyDto
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class CommentRemoteDataSource @Inject constructor(
    private val db: FirebaseFirestore
) : CommentDataSource {
    override fun observeByLifehack(lifehackId: String): Flow<List<CommentDto>> = callbackFlow {
        val subscription = db.collection(DBConstants.LIFEHACKS_COLLECTION)
            .document(lifehackId)
            .collection(DBConstants.COMMENTS_COLLECTION)
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
        dto: CommentDto
    ) {
        db.collection(DBConstants.LIFEHACKS_COLLECTION)
            .document(lifehackId)
            .collection(DBConstants.COMMENTS_COLLECTION)
            .add(dto).await()
    }

    override suspend fun addReply(
        lifehackId: String,
        commentId: String,
        dto: CommentReplyDto
    ) {
        db.collection(DBConstants.LIFEHACKS_COLLECTION)
            .document(lifehackId)
            .collection(DBConstants.COMMENTS_COLLECTION)
            .document(commentId)
            .collection(DBConstants.REPLIES_COLLECTION)
            .add(dto).await()
    }

    override suspend fun delete(
        lifehackId: String,
        commentId: String
    ) {
        db.collection(DBConstants.LIFEHACKS_COLLECTION)
            .document(lifehackId)
            .collection(DBConstants.COMMENTS_COLLECTION)
            .document(commentId)
            .delete().await()
    }

    override suspend fun deleteReply(
        lifehackId: String,
        commentId: String,
        replyId: String
    ) {
        db.collection(DBConstants.LIFEHACKS_COLLECTION)
            .document(lifehackId)
            .collection(DBConstants.COMMENTS_COLLECTION)
            .document(commentId)
            .collection(DBConstants.REPLIES_COLLECTION)
            .document(replyId)
            .delete().await()
    }
}