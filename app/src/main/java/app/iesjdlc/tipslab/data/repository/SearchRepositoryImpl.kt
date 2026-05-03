package app.iesjdlc.tipslab.data.repository

import app.iesjdlc.tipslab.data.datasource.remote.LifehackDataSource
import app.iesjdlc.tipslab.data.resolver.LifehackResolver
import app.iesjdlc.tipslab.domain.model.Lifehack
import app.iesjdlc.tipslab.domain.repository.SearchRepository
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val dataSource: LifehackDataSource,
    private val resolver: LifehackResolver
) : SearchRepository {
    override suspend fun searchLifehacksByCategory(
        categoryId: Int,
        query: String,
        limit: Int,
        offset: Int
    ): Result<List<Lifehack>> = runCatching {
        resolver.resolve(dataSource.searchByCategory(categoryId, query, limit, offset))
    }
}