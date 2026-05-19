package app.iesjdlc.tipslab.data.repository

import app.iesjdlc.tipslab.data.datasource.LifehackDataSource
import app.iesjdlc.tipslab.data.datasource.SearchDataSource
import app.iesjdlc.tipslab.data.resolver.LifehackResolver
import app.iesjdlc.tipslab.domain.model.Lifehack
import app.iesjdlc.tipslab.domain.repository.SearchRepository
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val localDataSource: SearchDataSource.Local,
    private val remoteDataSource: SearchDataSource.Remote,
    private val resolver: LifehackResolver
) : SearchRepository {
    override suspend fun getSearchHistory(userId: String): Result<List<String>> = runCatching {
        localDataSource.getSearchHistory(userId)
    }

    override suspend fun saveSearch(userId: String, query: String): Result<Unit> = runCatching {
        localDataSource.saveSearch(userId, query)
    }

    override suspend fun clearHistory(userId: String): Result<Unit> = runCatching {
        localDataSource.clearHistory(userId)
    }

    override suspend fun getSearchSuggestions(query: String, limit: Int): Result<List<String>> = runCatching {
        remoteDataSource.getSearchSuggestions(query, limit)
    }

    override suspend fun getSearchSuggestionsByCategory(categoryId: Int, query: String, limit: Int): Result<List<String>> = runCatching {
        remoteDataSource.getSearchSuggestionsByCategory(categoryId, query, limit)
    }

    override suspend fun searchLifehacks(query: String, limit: Int, offset: Int): Result<List<Lifehack>> = runCatching {
        resolver.resolve(remoteDataSource.searchLifehacksByQuery(query, limit))
    }

    override suspend fun searchLifehacksByCategory(
        categoryId: Int,
        query: String,
        limit: Int,
        offset: Int
    ): Result<List<Lifehack>> = runCatching {
        resolver.resolve(remoteDataSource.searchLifehacksByCategory(categoryId, query, limit, offset))
    }
}