package app.iesjdlc.tipslab.data.datasource

import app.iesjdlc.tipslab.data.model.LifehackDto

interface SearchDataSource {
    interface Local {
        suspend fun getSearchHistory(userId: String): List<String>
        suspend fun saveSearch(
            userId: String,
            query: String
        )
        suspend fun clearHistory(userId: String)
    }

    interface Remote {
        suspend fun getSearchSuggestions(
            query: String,
            limit: Int
        ): List<String>
        suspend fun getSearchSuggestionsByCategory(
            categoryId: Int,
            query: String,
            limit: Int
        ): List<String>
        suspend fun searchLifehacksByQuery(
            query: String,
            limit: Int,
            offset: Int = 0
        ): List<LifehackDto>
        suspend fun searchLifehacksByCategory(
            categoryId: Int,
            query: String,
            limit: Int,
            offset: Int = 0
        ): List<LifehackDto>
    }
}