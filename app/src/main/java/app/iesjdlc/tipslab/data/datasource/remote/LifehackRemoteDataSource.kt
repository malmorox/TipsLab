package app.iesjdlc.tipslab.data.datasource.remote

import app.iesjdlc.tipslab.data.model.LifehackDto
import app.iesjdlc.tipslab.domain.repository.OrderBy
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class LifehackRemoteDataSource @Inject constructor(
    private val db: FirebaseFirestore
) : LifehackDataSource {
    override suspend fun getByAuthor(authorId: String): List<LifehackDto> =
        db.collection("lifehacks")
            .whereEqualTo("author_id", authorId)
            .get().await()
            .documents
            .mapNotNull { it.toObject(LifehackDto::class.java) }

    override suspend fun getById(id: String): LifehackDto? =
        db.collection("lifehacks")
            .document(id)
            .get().await()
            .toObject(LifehackDto::class.java)

    override suspend fun getByIds(ids: List<String>): List<LifehackDto> =
        db.collection("lifehacks")
            .whereIn("id", ids)
            .get().await()
            .documents
            .mapNotNull { it.toObject(LifehackDto::class.java) }

    override suspend fun getByCategory(
        categoryId: Int,
        orderBy: OrderBy,
        limit: Int
    ): List<LifehackDto> =
        db.collection("lifehacks")
            .whereEqualTo("category_id", categoryId)
            .get().await()
            .documents
            .mapNotNull { it.toObject(LifehackDto::class.java) }

    override suspend fun searchByCategory(
        categoryId: Int,
        query: String,
        limit: Int,
        offset: Int
    ): List<LifehackDto> =
        db.collection("lifehacks")
            .whereEqualTo("category_id", categoryId)
            .whereArrayContains("tags", query)
            .get().await()
            .documents
            .mapNotNull { it.toObject(LifehackDto::class.java) }

    override suspend fun create(dto: LifehackDto): String {
        val docRef = db.collection("lifehacks").document()
        docRef.set(dto.copy(id = docRef.id)).await()
        return docRef.id
    }

    override suspend fun update(
        id: String,
        dto: LifehackDto
    ) {
        db.collection("lifehacks").document(id).set(dto).await()
    }

    override suspend fun updateMedia(
        lifehackId: String,
        mediaUrl: String,
        mediaType: String
    ) {
        db.collection("lifehacks").document(lifehackId).update(
            mapOf(
                "media_url" to mediaUrl,
                "media_type" to mediaType
            )
        ).await()
    }

    override suspend fun delete(id: String) {
        db.collection("lifehacks").document(id).delete().await()
    }
}