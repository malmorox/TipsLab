package app.iesjdlc.tipslab.usecases

import app.iesjdlc.tipslab.models.domain.Lifehack
import app.iesjdlc.tipslab.repository.LifehackRepository
import app.iesjdlc.tipslab.utils.LifehackResolver

class GetLifehacksUseCase(
    private val repository: LifehackRepository = LifehackRepository(),
    private val resolver: LifehackResolver = LifehackResolver()
) {
    suspend fun getMyLifehacks(): Result<List<Lifehack>> {
        try {
            val dtos = repository.getMyLifehacks().getOrThrow()
            val lifehacks = resolver.resolve(dtos)
            return Result.success(lifehacks)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    suspend fun getLifehackById(id: String): Result<Lifehack> {
        try {
            val dto = repository.getLifehackById(id).getOrThrow()
            val lifehack = resolver.resolveOne(dto) ?: return Result.failure(Exception("Lifehack no encontrado"))
            return Result.success(lifehack)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    suspend fun getByCategory(categoryId: String): Result<List<Lifehack>> {
        try {
            val dtos = repository.getLifehacksByCategory(categoryId).getOrThrow()
            val lifehacks = resolver.resolve(dtos)
            return Result.success(lifehacks)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }
}