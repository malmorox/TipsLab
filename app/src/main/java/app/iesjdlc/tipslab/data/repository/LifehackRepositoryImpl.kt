package app.iesjdlc.tipslab.data.repository

import app.iesjdlc.tipslab.data.datasource.LifehackDataSource
import app.iesjdlc.tipslab.data.mapper.LifehackMapper
import app.iesjdlc.tipslab.domain.model.Lifehack
import app.iesjdlc.tipslab.data.resolver.LifehackResolver
import app.iesjdlc.tipslab.domain.model.MediaType
import app.iesjdlc.tipslab.domain.repository.LifehackRepository
import app.iesjdlc.tipslab.domain.repository.OrderBy
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LifehackRepositoryImpl @Inject constructor(
    private val dataSource: LifehackDataSource,
    private val mapper: LifehackMapper,
    private val resolver: LifehackResolver
) : LifehackRepository {
    override suspend fun getUserLifehacks(uid: String): Result<List<Lifehack>> = runCatching {
        resolver.resolve(dataSource.getByAuthor(uid))
    }

    override suspend fun getLifehackById(id: String): Result<Lifehack> = runCatching {
        val dto = dataSource.getById(id) ?: error("Lifehack no encontrado")
        resolver.resolveOne(dto) ?: error("Lifehack no encontrado")
    }

    override suspend fun getLifehacksByCategory(
        categoryId: Int,
        orderBy: OrderBy,
        limit: Int
    ): Result<List<Lifehack>> = runCatching {
        resolver.resolve(dataSource.getByCategory(categoryId, orderBy, limit))
    }

    override suspend fun getRandomLifehacks(limit: Int): Result<List<Lifehack>> =
        Result.failure(NotImplementedError("getRandomLifehacks no implementado"))

    override fun observeLifehack(id: String): Flow<Lifehack> =
        dataSource.observeById(id)
            .map { dto -> resolver.resolveOne(dto) ?: error("Lifehack no encontrado") }

    override suspend fun createLifehack(lifehack: Lifehack): Result<String> = runCatching {
        dataSource.create(mapper.toDto(lifehack))
    }

    override suspend fun updateLifehack(
        id: String,
        lifehack: Lifehack
    ): Result<Unit> = runCatching {
        dataSource.update(id, mapper.toDto(lifehack))
    }

    override suspend fun updateLifehackMedia(
        lifehackId: String,
        mediaUrl: String,
        mediaType: MediaType
    ): Result<Unit> = runCatching {
        dataSource.updateMedia(lifehackId, mediaUrl, mediaType.name)
    }


    override suspend fun deleteLifehack(id: String): Result<Unit> = runCatching {
        dataSource.delete(id)
    }
}