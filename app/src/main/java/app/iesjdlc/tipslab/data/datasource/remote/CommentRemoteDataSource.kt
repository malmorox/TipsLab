package app.iesjdlc.tipslab.data.datasource.remote

import app.iesjdlc.tipslab.core.constants.DBConstants
import app.iesjdlc.tipslab.data.datasource.CommentDataSource
import app.iesjdlc.tipslab.data.model.CommentDto
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
    private fun lifehackRef(lifehackId: String) =
        db.collection(DBConstants.Remote.LIFEHACKS_COLLECTION).document(lifehackId)

    private fun commentsRef(lifehackId: String) =
        lifehackRef(lifehackId).collection(DBConstants.Remote.COMMENTS_SUBCOLLECTION)

    private fun commentRef(lifehackId: String, commentId: String) =
        commentsRef(lifehackId).document(commentId)

    override fun observeByLifehack(lifehackId: String): Flow<List<CommentDto>> = callbackFlow {
        val subscription = commentsRef(lifehackId)
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
        val docRef = commentsRef(lifehackId).document()
        val dtoWithId = dto.copy(id = docRef.id)

        db.runTransaction { transaction ->
            transaction.update(lifehackRef(lifehackId), DBConstants.Remote.COMMENTS_COUNT_FIELD, FieldValue.increment(1))
            transaction.set(docRef, dtoWithId)
        }.await()
    }

    override suspend fun delete(
        lifehackId: String,
        commentId: String
    ) {
        db.runTransaction { transaction ->
            transaction.update(lifehackRef(lifehackId), DBConstants.Remote.COMMENTS_COUNT_FIELD, FieldValue.increment(-1))
            transaction.delete(commentRef(lifehackId, commentId))
        }.await()
    }
}