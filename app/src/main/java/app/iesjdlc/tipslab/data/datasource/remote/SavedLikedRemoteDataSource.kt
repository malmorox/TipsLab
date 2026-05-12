package app.iesjdlc.tipslab.data.datasource.remote

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class SavedLikedRemoteDataSource @Inject constructor(
    private val db: FirebaseFirestore
) : SavedLikedDataSource {
    private fun savedCollection(userId: String) =
        db.collection("users").document(userId).collection("saved")

    private fun likedCollection(userId: String) =
        db.collection("users").document(userId).collection("liked")

    override suspend fun getSavedIds(userId: String): List<String> =
        savedCollection(userId)
            .orderBy("saved_at", Query.Direction.DESCENDING)
            .get().await()
            .documents.map { it.id }

    override suspend fun getLikedIds(userId: String): List<String> =
        likedCollection(userId)
            .orderBy("liked_at", Query.Direction.DESCENDING)
            .get().await()
            .documents.map { it.id }

    override suspend fun isLifehackSaved(userId: String, lifehackId: String): Boolean =
        savedCollection(userId).document(lifehackId).get().await().exists()

    override suspend fun isLifehackLiked(userId: String, lifehackId: String): Boolean =
        likedCollection(userId).document(lifehackId).get().await().exists()

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
                transaction.update(lifehackRef, "liked_count", FieldValue.increment(-1))
            } else {
                transaction.set(likedRef, mapOf("liked_at" to System.currentTimeMillis()))
                transaction.update(lifehackRef, "liked_count", FieldValue.increment(1))
            }
        }.await()

        return !exists
    }

    override suspend fun getLikedCount(userId: String): Int =
        likedCollection(userId).get().await().size()
}