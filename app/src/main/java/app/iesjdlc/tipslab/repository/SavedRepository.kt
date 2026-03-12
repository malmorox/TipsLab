package app.iesjdlc.tipslab.repository

import app.iesjdlc.tipslab.utils.FirebaseClient
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

class SavedRepository {
    private val db = FirebaseClient.db
    private val auth = FirebaseClient.auth

    private fun savedCollection(userId: String) = db.collection("users").document(userId).collection("saved")

    suspend fun getSavedIds(): Result<List<String>> {
        return try {
            val userId = auth.currentUser?.uid
                ?: return Result.failure(Exception("No user logged in"))

            val snapshot = savedCollection(userId)
                .orderBy("saved_at", Query.Direction.DESCENDING)
                .get()
                .await()

            Result.success(snapshot.documents.map { it.id })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun isSaved(lifehackId: String): Result<Boolean> {
        return try {
            val userId = auth.currentUser?.uid
                ?: return Result.failure(Exception("No user logged in"))

            val exists = savedCollection(userId).document(lifehackId).get().await().exists()
            Result.success(exists)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun toggleSaved(lifehackId: String): Result<Boolean> {
        return try {
            val userId = auth.currentUser?.uid
                ?: return Result.failure(Exception("No user logged in"))

            val savedRef = savedCollection(userId).document(lifehackId)
            val lifehackRef = db.collection("lifehacks").document(lifehackId)

            val exists = savedRef.get().await().exists()

            // Transacción para mantener consistencia entre ambos documentos
            db.runTransaction { transaction ->
                if (exists) {
                    transaction.delete(savedRef)
                    transaction.update(lifehackRef, "saved_count", com.google.firebase.firestore.FieldValue.increment(-1))
                } else {
                    transaction.set(savedRef, mapOf("saved_at" to System.currentTimeMillis()))
                    transaction.update(lifehackRef, "saved_count", com.google.firebase.firestore.FieldValue.increment(1))
                }
            }.await()

            Result.success(!exists)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getSavedCount(): Result<Int> {
        return try {
            val userId = auth.currentUser?.uid
                ?: return Result.failure(Exception("No user logged in"))

            val count = savedCollection(userId).get().await().size()
            Result.success(count)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}