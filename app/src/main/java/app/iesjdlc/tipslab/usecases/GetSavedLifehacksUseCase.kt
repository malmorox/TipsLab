package app.iesjdlc.tipslab.usecases

import app.iesjdlc.tipslab.models.domain.Lifehack
import app.iesjdlc.tipslab.repository.SavedRepository
import app.iesjdlc.tipslab.utils.LifehackResolver

class GetSavedLifehacksUseCase(
    private val repository: SavedRepository = SavedRepository(),
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