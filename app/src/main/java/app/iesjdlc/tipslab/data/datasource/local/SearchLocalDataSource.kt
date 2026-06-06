package app.iesjdlc.tipslab.data.datasource.local

import app.iesjdlc.tipslab.data.datasource.SearchDataSource
import app.iesjdlc.tipslab.data.datasource.local.db.SearchDao
import app.iesjdlc.tipslab.data.datasource.local.db.SearchEntity
import javax.inject.Inject

class SearchLocalRoomDataSource @Inject constructor(
    private val dao: SearchDao
) : SearchDataSource.Local {
    override suspend fun getSearchHistory(userId: String): List<String> =
        dao.getRecentSearches(userId).map { it.query }

    override suspend fun saveSearch(userId: String, query: String) {
        dao.insert(
            SearchEntity(
                id = 0,
                userId = userId,
                query = query
            )
        )
    }

    override suspend fun clearHistory(userId: String) {
        dao.clearAll(userId)
    }
}