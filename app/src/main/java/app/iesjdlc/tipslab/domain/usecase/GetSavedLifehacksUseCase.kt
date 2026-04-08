package app.iesjdlc.tipslab.domain.usecase

import app.iesjdlc.tipslab.domain.model.Lifehack
import app.iesjdlc.tipslab.data.repository.SavedRepositoryImpl
import app.iesjdlc.tipslab.data.resolver.LifehackResolver

class GetSavedLifehacksUseCase(
    private val repository: SavedRepositoryImpl = SavedRepositoryImpl(),
    private val resolver: LifehackResolver = LifehackResolver()
) {
    suspend operator fun invoke(): Result<List<Lifehack>> {
        return try {
            val savedLifehacks = repository.getSavedLifehacks()
                .getOrElse { return Result.failure(it) }

            val lifehacks = resolver.resolve(savedLifehacks)

            Result.success(lifehacks)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}