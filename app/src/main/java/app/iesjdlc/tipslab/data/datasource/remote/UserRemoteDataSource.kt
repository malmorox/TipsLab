package app.iesjdlc.tipslab.data.datasource.remote

import app.iesjdlc.tipslab.core.constants.DBConstants
import app.iesjdlc.tipslab.data.model.UserDto
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRemoteDataSource @Inject constructor(
    private val db: FirebaseFirestore
) : UserDataSource {
    override suspend fun existsById(uid: String): Boolean =
        db.collection(DBConstants.USERS_COLLECTION)
            .document(uid)
            .get().await().exists()

    override suspend fun existsByEmail(email: String): Boolean =
        !db.collection(DBConstants.USERS_COLLECTION)
            .whereEqualTo(DBConstants.EMAIL_FIELD, email)
            .get().await().isEmpty

    override suspend fun existsByUsername(username: String): Boolean =
        !db.collection(DBConstants.USERS_COLLECTION)
            .whereEqualTo(DBConstants.USERNAME_FIELD, username)
            .get().await().isEmpty

    override suspend fun getEmailByUsername(username: String): String? =
        db.collection(DBConstants.USERS_COLLECTION)
            .whereEqualTo(DBConstants.USERNAME_FIELD, username)
            .get().await()
            .documents.firstOrNull()?.getString(DBConstants.EMAIL_FIELD)

    override suspend fun getById(id: String): UserDto? =
        db.collection(DBConstants.USERS_COLLECTION)
            .document(id)
            .get().await()
            .toObject(UserDto::class.java)

    override suspend fun getByIds(ids: List<String>): Map<String, UserDto> {
        if (ids.isEmpty()) return emptyMap()
        return db.collection(DBConstants.USERS_COLLECTION)
            .whereIn(FieldPath.documentId(), ids)
            .get().await()
            .documents
            .mapNotNull { doc ->
                doc.toObject(UserDto::class.java)?.let { doc.id to it }
            }
            .toMap()
    }

    override suspend fun save(dto: UserDto) {
        db.collection(DBConstants.USERS_COLLECTION)
            .document(dto.id)
            .set(dto).await()
    }

    override suspend fun update(id: String, dto: UserDto) {
        db.collection(DBConstants.USERS_COLLECTION)
            .document(id)
            .set(dto).await()
    }
}