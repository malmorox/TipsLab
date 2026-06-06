package app.iesjdlc.tipslab.data.datasource.remote

import app.iesjdlc.tipslab.core.constants.DBConstants
import app.iesjdlc.tipslab.data.datasource.SavedLikedDataSource
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class SavedLikedRemoteDataSource @Inject constructor(
    private val db: FirebaseFirestore
) : SavedLikedDataSource {
    private fun savedCollection(userId: String) =
        db.collection(DBConstants.Remote.USERS_COLLECTION)
            .document(userId)
            .collection(DBConstants.Remote.SAVED_SUBCOLLECTION)

    private fun likedCollection(userId: String) =
        db.collection(DBConstants.Remote.USERS_COLLECTION)
            .document(userId)
            .collection(DBConstants.Remote.LIKED_SUBCOLLECTION)

    override fun observeSavedIds(userId: String): Flow<List<String>> = callbackFlow {
        val listener = savedCollection(userId)
            .orderBy("saved_at", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                trySend(snapshot?.documents?.map { it.id } ?: emptyList())
            }
        awaitClose { listener.remove() }
    }

    override fun observeLikedIds(userId: String): Flow<List<String>> = callbackFlow {
        val listener = likedCollection(userId)
            .orderBy("liked_at", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                trySend(snapshot?.documents?.map { it.id } ?: emptyList())
            }
        awaitClose { listener.remove() }
    }

    override fun observeIsLiked(userId: String, lifehackId: String): Flow<Boolean> = callbackFlow {
        val listener = likedCollection(userId)
            .document(lifehackId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                trySend(snapshot?.exists() ?: false)
            }
        awaitClose { listener.remove() }
    }

    override fun observeIsSaved(userId: String, lifehackId: String): Flow<Boolean> = callbackFlow {
        val listener = savedCollection(userId)
            .document(lifehackId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                trySend(snapshot?.exists() ?: false)
            }
        awaitClose { listener.remove() }
    }

    override suspend fun toggleSaved(userId: String, lifehackId: String): Boolean {
        val savedRef = savedCollection(userId).document(lifehackId)
        val lifehackRef = db.collection("lifehacks").document(lifehackId)
        val exists = savedRef.get().await().exists()

        db.runTransaction { transaction ->
            if (exists) {
                transaction.delete(savedRef)
                transaction.update(lifehackRef, "saved_count", FieldValue.increment(-1))
            } else {
                transaction.set(savedRef, mapOf("saved_at" to System.currentTimeMillis()))
                transaction.update(lifehackRef, "saved_count", FieldValue.increment(1))
            }
        }.await()

        return !exists
    }

    override suspend fun toggleLiked(userId: String, lifehackId: String): Boolean {
        val likedRef = likedCollection(userId).document(lifehackId)
        val lifehackRef = db.collection("lifehacks").document(lifehackId)
        val exists = likedRef.get().await().exists()

        db.runTransaction { transaction ->
            if (exists) {
                transaction.delete(likedRef)
                transaction.update(lifehackRef, "likes_count", FieldValue.increment(-1))
            } else {
                transaction.set(likedRef, mapOf("liked_at" to System.currentTimeMillis()))
                transaction.update(lifehackRef, "likes_count", FieldValue.increment(1))
            }
        }.await()

        return !exists
    }

    override suspend fun getLikedCount(userId: String): Int =
        likedCollection(userId).get().await().size()
}