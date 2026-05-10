package app.iesjdlc.tipslab.domain.repository

import app.iesjdlc.tipslab.domain.model.Lifehack
import app.iesjdlc.tipslab.domain.model.MediaType

interface LifehackRepository {
    suspend fun getUserLifehacks(uid: String): Result<List<Lifehack>>
    suspend fun getLifehackById(id: String): Result<Lifehack>
    suspend fun getLifehacksByCategory(
        categoryId: Int,
        orderBy: OrderBy,
        limit: Int
    ): Result<List<Lifehack>>
    suspend fun getRandomLifehacks(limit: Int = 10): Result<List<Lifehack>>
    suspend fun createLifehack(lifehack: Lifehack): Result<String>
    suspend fun updateLifehack(
        id: String,
        lifehack: Lifehack
    ): Result<Unit>
    suspend fun updateLifehackMedia(
        lifehackId: String,
        mediaUrl: String,
        mediaType: MediaType
    ): Result<Unit>
    suspend fun deleteLifehack(id: String): Result<Unit>
}

enum class OrderBy {
    RECENT,
    POPULAR
}