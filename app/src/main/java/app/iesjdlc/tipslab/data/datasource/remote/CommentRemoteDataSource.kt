package app.iesjdlc.tipslab.data.datasource.remote

import app.iesjdlc.tipslab.core.constants.DBConstants
import app.iesjdlc.tipslab.data.datasource.CommentDataSource
import app.iesjdlc.tipslab.data.model.CommentDto
import app.iesjdlc.tipslab.data.model.CommentReplyDto
import com.google.firebase.firestore.FieldValue
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
        val subscription = db.collection(DBConstants.Remote.LIFEHACKS_COLLECTION)
            .document(lifehackId)
            .collection(DBConstants.Remote.COMMENTS_SUBCOLLECTION)
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
        val lifehackRef = db.collection(DBConstants.Remote.LIFEHACKS_COLLECTION)
            .document(lifehackId)
        val commentsRef = lifehackRef.collection(DBConstants.Remote.COMMENTS_SUBCOLLECTION)

        db.runTransaction { transaction ->
            transaction.update(lifehackRef, DBConstants.Remote.COMMENTS_COUNT_FIELD, FieldValue.increment(1))
            transaction.set(commentsRef.document(), dto)
        }.await()
    }

    override suspend fun addReply(
        lifehackId: String,
        commentId: String,
        dto: CommentReplyDto
    ) {
        val commentRef = db.collection(DBConstants.Remote.LIFEHACKS_COLLECTION)
            .document(lifehackId)
            .collection(DBConstants.Remote.COMMENTS_SUBCOLLECTION)
            .document(commentId)
        val repliesRef = commentRef.collection(DBConstants.Remote.REPLIES_SUBCOLLECTION)

        db.runTransaction { transaction ->
            transaction.update(commentRef, DBConstants.Remote.REPLIES_COUNT_FIELD, FieldValue.increment(1))
            transaction.set(repliesRef.document(), dto)
        }.await()
    }

    override suspend fun delete(
        lifehackId: String,
        commentId: String
    ) {
        val lifehackRef = db.collection(DBConstants.Remote.LIFEHACKS_COLLECTION)
            .document(lifehackId)
        val commentRef = lifehackRef
            .collection(DBConstants.Remote.COMMENTS_SUBCOLLECTION)
            .document(commentId)

        db.runTransaction { transaction ->
            transaction.update(lifehackRef, DBConstants.Remote.COMMENTS_COUNT_FIELD, FieldValue.increment(-1))
            transaction.delete(commentRef)
        }.await()
    }

    override suspend fun deleteReply(
        lifehackId: String,
        commentId: String,
        replyId: String
    ) {
        val commentRef = db.collection(DBConstants.Remote.LIFEHACKS_COLLECTION)
            .document(lifehackId)
            .collection(DBConstants.Remote.COMMENTS_SUBCOLLECTION)
            .document(commentId)
        val replyRef = commentRef
            .collection(DBConstants.Remote.REPLIES_SUBCOLLECTION)
            .document(replyId)

        db.runTransaction { transaction ->
            transaction.update(commentRef, DBConstants.Remote.REPLIES_COUNT_FIELD, FieldValue.increment(-1))
            transaction.delete(replyRef)
        }.await()
    }
}