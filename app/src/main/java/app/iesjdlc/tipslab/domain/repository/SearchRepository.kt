package app.iesjdlc.tipslab.domain.repository

import app.iesjdlc.tipslab.domain.model.Lifehack

interface SearchRepository {
    suspend fun searchLifehacksByCategory(
        categoryId: Int,
        query: String,
        limit: Int = 5,
        offset: Int = 0
    ): Result<List<Lifehack>>
}