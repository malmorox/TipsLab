package app.iesjdlc.tipslab.usecases

import app.iesjdlc.tipslab.models.domain.Lifehack
import app.iesjdlc.tipslab.repository.CategoryRepository
import app.iesjdlc.tipslab.repository.LifehackRepository
import app.iesjdlc.tipslab.repository.SavedRepository
import app.iesjdlc.tipslab.repository.UserRepository
import app.iesjdlc.tipslab.utils.LifehackUtils

class GetSavedLifehacksUseCase(
    private val savedRepository: SavedRepository = SavedRepository(),
    private val lifehackRepository: LifehackRepository = LifehackRepository(),
    private val utils: LifehackUtils = LifehackUtils(UserRepository(), CategoryRepository())
) {
    suspend fun getSavedLifehacks(): Result<List<Lifehack>> {
        return try {
            val savedIds = savedRepository.getSavedIds().getOrThrow()
            val lifehacksDto = savedIds.mapNotNull { id ->
                lifehackRepository.getLifehackById(id).getOrNull()
            }

            val lifehacks = lifehacksDto.mapNotNull { dto ->
                utils.mapToLifehack(dto)
            }

            Result.success(lifehacks)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}