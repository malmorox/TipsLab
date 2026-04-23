package app.iesjdlc.tipslab.data.repository

import app.iesjdlc.tipslab.data.datasource.remote.LifehackDataSource
import app.iesjdlc.tipslab.data.mapper.LifehackMapper
import app.iesjdlc.tipslab.domain.model.Lifehack
import app.iesjdlc.tipslab.data.resolver.LifehackResolver
import app.iesjdlc.tipslab.domain.repository.LifehackRepository
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class LifehackRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val dataSource: LifehackDataSource,
    private val mapper: LifehackMapper,
    private val resolver: LifehackResolver
) : LifehackRepository {
    override suspend fun getMyLifehacks(): Result<List<Lifehack>> = runCatching {
        val uid = auth.currentUser?.uid ?: error("No autenticado")
        resolver.resolve(dataSource.getByAuthor(uid))
    }

    override suspend fun getLifehackById(id: String): Result<Lifehack> = runCatching {
        val dto = dataSource.getById(id) ?: error("Lifehack no encontrado")
        resolver.resolveOne(dto) ?: error("No se pudo resolver el lifehack")
    }

    override suspend fun getLifehacksByCategory(categoryId: Int): Result<List<Lifehack>> = runCatching {
        resolver.resolve(dataSource.getByCategory(categoryId))
    }

    override suspend fun getRandomLifehacks(limit: Int): Result<List<Lifehack>> =
        Result.failure(NotImplementedError("getRandomLifehacks no implementado"))

    override suspend fun createLifehack(lifehack: Lifehack): Result<String> = runCatching {
        dataSource.create(mapper.toDto(lifehack))
    }

    override suspend fun updateLifehack(id: String, lifehack: Lifehack): Result<Unit> = runCatching {
        dataSource.update(id, mapper.toDto(lifehack))
    }

    override suspend fun deleteLifehack(id: String): Result<Unit> = runCatching {
        dataSource.delete(id)
    }
}