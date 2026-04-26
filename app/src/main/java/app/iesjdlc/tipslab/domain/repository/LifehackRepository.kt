package app.iesjdlc.tipslab.domain.repository

import app.iesjdlc.tipslab.domain.model.Lifehack

interface LifehackRepository {
    suspend fun getMyLifehacks(): Result<List<Lifehack>>
    suspend fun getLifehackById(id: String): Result<Lifehack>
    suspend fun getLifehacksByCategory(categoryId: Int): Result<List<Lifehack>>
    suspend fun getRandomLifehacks(limit: Int = 10): Result<List<Lifehack>>
    suspend fun isLifehackLiked(userId: String, lifehackId: String): Result<Boolean>
    suspend fun createLifehack(lifehack: Lifehack): Result<String>
    suspend fun updateLifehack(id: String, lifehack: Lifehack): Result<Unit>
    suspend fun deleteLifehack(id: String): Result<Unit>
}