package app.iesjdlc.tipslab.data.repository

import app.iesjdlc.tipslab.data.model.LifehackDto
import app.iesjdlc.tipslab.data.remote.firebase.FirebaseClient
import app.iesjdlc.tipslab.data.resolver.LifehackResolver
import app.iesjdlc.tipslab.domain.model.Lifehack
import app.iesjdlc.tipslab.domain.repository.SavedRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

class SavedRepositoryImpl(
    private val db: FirebaseFirestore = FirebaseClient.db,
    private val auth: FirebaseAuth = FirebaseClient.auth,
    private val resolver: LifehackResolver = LifehackResolver()
) : SavedRepository {
    private fun savedCollection(userId: String) = db.collection("users").document(userId).collection("saved")

    private suspend fun getSavedIds(): Result<List<String>> {
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

    override suspend fun getSavedLifehacks(): Result<List<Lifehack>> {
        return try {
            val savedIds = getSavedIds().getOrThrow()
            val lifehacks = db.collection("lifehacks")
                .whereIn("id", savedIds)
                .get()
                .await()
            val dtos =
                lifehacks.documents.mapNotNull { doc -> doc.toObject(LifehackDto::class.java) }
            Result.success(resolver.resolve(dtos))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun isSaved(lifehackId: String): Result<Boolean> {
        return try {
            val userId = auth.currentUser?.uid
                ?: return Result.failure(Exception("No user logged in"))

            val exists = savedCollection(userId).document(lifehackId).get().await().exists()
            Result.success(exists)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun toggleSaved(lifehackId: String): Result<Boolean> {
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
                    transaction.update(lifehackRef, "saved_count", FieldValue.increment(-1))
                } else {
                    transaction.set(savedRef, mapOf("saved_at" to System.currentTimeMillis()))
                    transaction.update(lifehackRef, "saved_count", FieldValue.increment(1))
                }
            }.await()

            Result.success(!exists)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getSavedCount(): Result<Int> {
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