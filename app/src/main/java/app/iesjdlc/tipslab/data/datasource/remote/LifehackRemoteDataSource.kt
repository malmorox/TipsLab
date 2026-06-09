package app.iesjdlc.tipslab.data.datasource.remote

import app.iesjdlc.tipslab.core.constants.DBConstants
import app.iesjdlc.tipslab.data.datasource.LifehackDataSource
import app.iesjdlc.tipslab.data.model.LifehackDto
import app.iesjdlc.tipslab.domain.repository.OrderBy
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class LifehackRemoteDataSource @Inject constructor(
    private val db: FirebaseFirestore
) : LifehackDataSource {
    override suspend fun getById(id: String): LifehackDto? =
        db.collection(DBConstants.Remote.LIFEHACKS_COLLECTION)
            .document(id)
            .get().await()
            .toObject(LifehackDto::class.java)

    override suspend fun getByIds(ids: List<String>): List<LifehackDto> =
        db.collection(DBConstants.Remote.LIFEHACKS_COLLECTION)
            .whereIn(DBConstants.Remote.ID_FIELD, ids)
            .get().await()
            .documents
            .mapNotNull { it.toObject(LifehackDto::class.java) }

    override suspend fun get(
        orderBy: OrderBy,
        limit: Int
    ): List<LifehackDto> {
        val field = when (orderBy) {
            OrderBy.RECENT -> DBConstants.Remote.CREATED_AT_FIELD
            OrderBy.POPULAR -> DBConstants.Remote.LIKES_COUNT_FIELD
        }
        return db.collection(DBConstants.Remote.LIFEHACKS_COLLECTION)
            .orderBy(field, Query.Direction.DESCENDING)
            .limit(limit.toLong())
            .get().await()
            .documents
            .mapNotNull { it.toObject(LifehackDto::class.java) }
    }

    override suspend fun getByCategory(
        categoryId: Int,
        orderBy: OrderBy,
        limit: Int
    ): List<LifehackDto> {
        val field = when (orderBy) {
            OrderBy.RECENT -> DBConstants.Remote.CREATED_AT_FIELD
            OrderBy.POPULAR -> DBConstants.Remote.LIKES_COUNT_FIELD
        }
        return db.collection(DBConstants.Remote.LIFEHACKS_COLLECTION)
            .whereEqualTo(DBConstants.Remote.CATEGORY_ID_FIELD, categoryId)
            .orderBy(field, Query.Direction.DESCENDING)
            .limit(limit.toLong())
            .get().await()
            .documents
            .mapNotNull { it.toObject(LifehackDto::class.java) }
    }

    override fun observeById(id: String): Flow<LifehackDto> = callbackFlow {
        val subscription = db.collection(DBConstants.Remote.LIFEHACKS_COLLECTION)
            .document(id)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                snapshot?.toObject(LifehackDto::class.java)?.let { trySend(it) }
            }
        awaitClose { subscription.remove() }
    }

    override fun observeByAuthor(authorId: String): Flow<List<LifehackDto>> = callbackFlow {
        val subscription = db.collection(DBConstants.Remote.LIFEHACKS_COLLECTION)
            .whereEqualTo(DBConstants.Remote.AUTHOR_ID_FIELD, authorId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val lifehacks = snapshot?.documents
                    ?.mapNotNull { it.toObject(LifehackDto::class.java) }
                    ?: emptyList()
                trySend(lifehacks)
            }
        awaitClose { subscription.remove() }
    }

    override suspend fun create(dto: LifehackDto): String {
        val docRef = db.collection(DBConstants.Remote.LIFEHACKS_COLLECTION).document()
        docRef.set(dto.copy(id = docRef.id)).await()
        return docRef.id
    }

    override suspend fun update(
        id: String,
        dto: LifehackDto
    ) {
        db.collection(DBConstants.Remote.LIFEHACKS_COLLECTION)
            .document(id)
            .set(dto).await()
    }

    override suspend fun updateMedia(
        lifehackId: String,
        mediaUrl: String,
        mediaType: String
    ) {
        db.collection(DBConstants.Remote.LIFEHACKS_COLLECTION)
            .document(lifehackId)
            .update(
            mapOf(
                DBConstants.Remote.MEDIA_URL_FIELD to mediaUrl,
                DBConstants.Remote.MEDIA_TYPE_FIELD to mediaType
            )
        ).await()
    }

    override suspend fun delete(id: String) {
        db.collection(DBConstants.Remote.LIFEHACKS_COLLECTION)
            .document(id)
            .delete().await()
    }
}