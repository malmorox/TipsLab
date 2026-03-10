package app.iesjdlc.tipslab.repository

import app.iesjdlc.tipslab.utils.FirebaseClient
import kotlinx.coroutines.tasks.await

class FavoritesRepository {
    private val db = FirebaseClient.db

    suspend fun getFavoriteLifehacksByUser(userId: String): List<String> {
        val snapshot = db.collection("favorites")
            .whereEqualTo("userId", userId)
            .get()
            .await()

        return snapshot.documents.mapNotNull { it.getString("lifehackId") }
    }

    suspend fun addFavorite(userId: String, lifehackId: String) {
        val favorite = hashMapOf(
            "userId" to userId,
            "lifehackId" to lifehackId
        )
        db.collection("favorites").add(favorite).await()
    }

    suspend fun removeFavorite(userId: String, lifehackId: String) {
        val snapshot = db.collection("favorites")
            .whereEqualTo("userId", userId)
            .whereEqualTo("lifehackId", lifehackId)
            .get()
            .await()

        for (doc in snapshot.documents) {
            db.collection("favorites").document(doc.id).delete().await()
        }
    }
}