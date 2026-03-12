package app.iesjdlc.tipslab.utils

import kotlinx.coroutines.tasks.await

class AuthUtils {
    private val db = FirebaseClient.db

    suspend fun existsEmail(email: String): Boolean {
        val snapshot = db.collection("users")
            .whereEqualTo("email", email)
            .get()
            .await()
        return !snapshot.isEmpty
    }

    suspend fun existsUsername(username: String): Boolean {
        val snapshot = db.collection("users")
            .whereEqualTo("username", username)
            .get()
            .await()
        return !snapshot.isEmpty
    }

    suspend fun getEmailByUsername(username: String): String? {
        val snapshot = db.collection("users")
            .whereEqualTo("username", username)
            .get()
            .await()
        return snapshot.documents.firstOrNull()?.getString("email")
    }
}