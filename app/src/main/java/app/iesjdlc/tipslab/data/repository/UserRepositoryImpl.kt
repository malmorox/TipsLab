package app.iesjdlc.tipslab.data.repository

import app.iesjdlc.tipslab.data.mapper.UserMapper
import app.iesjdlc.tipslab.domain.model.User
import app.iesjdlc.tipslab.data.model.UserDto
import app.iesjdlc.tipslab.data.remote.firebase.FirebaseClient
import app.iesjdlc.tipslab.domain.repository.UserRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserRepositoryImpl(
    private val db: FirebaseFirestore = FirebaseClient.db,
    private val mapper: UserMapper = UserMapper()
) : UserRepository {
    override suspend fun existsUser(uid: String): Result<Boolean> {
        return try {
            val doc = db.collection("users").document(uid).get().await()
            Result.success(doc.exists())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun existsEmail(email: String): Result<Boolean> {
        return try {
            val snapshot = db.collection("users")
                .whereEqualTo("email", email)
                .get()
                .await()
            Result.success(!snapshot.isEmpty)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun existsUsername(username: String): Result<Boolean> {
        return try {
            val snapshot = db.collection("users")
                .whereEqualTo("username", username)
                .get()
                .await()
            Result.success(!snapshot.isEmpty)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getEmailByUsername(username: String): Result<String?> {
        return try {
            val snapshot = db.collection("users")
                .whereEqualTo("username", username)
                .get()
                .await()
            Result.success(snapshot.documents.firstOrNull()?.getString("email"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getUserById(id: String): Result<User> {
        return try {
            val doc = db.collection("users").document(id).get().await()
            doc.toObject(UserDto::class.java)?.let { Result.success(mapper.toDomain(it)) }
                ?: Result.failure(Exception("Usuario no encontrado"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Función para crear un nuevo usuario en Firestore
    override suspend fun createUser(user: User): Result<Unit> {
        return try {
            val dto = mapper.toDto(user)
            db.collection("users").document(user.id).set(dto).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Crea el documento de un nuevo usuario de Google en Firestore
    override suspend fun createGoogleUser(user: User): Result<Unit> {
        return try {
            val dto = mapper.toDto(user)
            db.collection("users").document(dto.id).set(dto).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateUserProfile(user: User): Result<Unit> {
        return try {
            val dto = mapper.toDto(user)
            db.collection("users").document(user.id).set(dto).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}