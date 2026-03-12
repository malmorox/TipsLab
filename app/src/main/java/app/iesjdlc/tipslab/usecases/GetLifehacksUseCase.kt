package app.iesjdlc.tipslab.usecases

import app.iesjdlc.tipslab.models.domain.Lifehack
import app.iesjdlc.tipslab.repository.CategoryRepository
import app.iesjdlc.tipslab.repository.LifehackRepository
import app.iesjdlc.tipslab.repository.UserRepository
import app.iesjdlc.tipslab.utils.LifehackUtils

class GetLifehacksUseCase(
    private val repository: LifehackRepository = LifehackRepository(),
    private val utils: LifehackUtils = LifehackUtils(UserRepository(), CategoryRepository())
) {
    suspend fun getMyLifehacks(): Result<List<Lifehack>> {
        return try {
            val dtos = repository.getMyLifehacks().getOrThrow()
            Result.success(dtos.mapNotNull { dto -> utils.mapToLifehack(dto) })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getLifehackById(id: String): Result<Lifehack> {
        return try {
            val dto = repository.getLifehackById(id).getOrThrow()
            utils.mapToLifehack(dto)?.let { Result.success(it) }
                ?: Result.failure(Exception("Lifehack no encontrado"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getByCategory(categoryId: String): Result<List<Lifehack>> {
        return try {
            val dtos = repository.getLifehacksByCategory(categoryId).getOrThrow()
            Result.success(dtos.mapNotNull { dto -> utils.mapToLifehack(dto) })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}