package app.iesjdlc.tipslab.domain.repository

import app.iesjdlc.tipslab.domain.model.Lifehack

interface SearchRepository {
    suspend fun getSearchHistory(userId: String): Result<List<String>>
    suspend fun saveSearch(userId: String, query: String): Result<Unit>
    suspend fun clearHistory(userId: String): Result<Unit>
    suspend fun getSearchSuggestions(
        query: String,
        limit: Int = 10
    ): Result<List<String>>
    suspend fun getSearchSuggestionsByCategory(
        categoryId: Int,
        query: String,
        limit: Int = 10
    ): Result<List<String>>
    suspend fun searchLifehacks(
        query: String,
        limit: Int = 20,
        offset: Int = 0
    ): Result<List<Lifehack>>
    suspend fun searchLifehacksByCategory(
        categoryId: Int,
        query: String,
        limit: Int = 20,
        offset: Int = 0
    ): Result<List<Lifehack>>
}