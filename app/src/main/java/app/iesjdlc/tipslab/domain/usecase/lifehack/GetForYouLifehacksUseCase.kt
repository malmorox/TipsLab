package app.iesjdlc.tipslab.domain.usecase.lifehack

import app.iesjdlc.tipslab.domain.model.Lifehack
import app.iesjdlc.tipslab.domain.repository.AuthRepository
import app.iesjdlc.tipslab.domain.repository.LifehackRepository
import app.iesjdlc.tipslab.domain.repository.SavedLikedRepository
import app.iesjdlc.tipslab.domain.repository.SearchRepository
import app.iesjdlc.tipslab.domain.repository.OrderBy
import javax.inject.Inject

/*class GetForYouLifehacksUseCase @Inject constructor(
    private val savedLikedRepository: SavedLikedRepository,
    private val lifehackRepository: LifehackRepository,
    private val searchRepository: SearchRepository,
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(limit: Int = 10): Result<List<Lifehack>> = runCatching {
        val userId = authRepository.getCurrentUser().id

        // 1. Coge liked y saved en paralelo
        val likedLifehacks = savedLikedRepository.getUserLikedLifehacks(userId).getOrElse { emptyList() }
        val savedLifehacks = savedLikedRepository.getUserSavedLifehacks(userId).getOrElse { emptyList() }

        // 2. Extrae categorías de liked y saved
        val categoryIds = (likedLifehacks + savedLifehacks)
            .map { it.categoryId }
            .distinct()

        // 3. Coge historial de búsqueda
        val searchHistory = searchRepository.getSearchHistory(userId).getOrElse { emptyList() }

        // 4. Si no hay nada → randoms
        if (categoryIds.isEmpty() && searchHistory.isEmpty()) {
            return@runCatching lifehackRepository.getLifehacks(OrderBy.RECENT, limit)
                .getOrElse { emptyList() }
                .shuffled()
                .take(limit)
        }

        val results = mutableListOf<Lifehack>()

        // 5. Lifehacks por categorías de liked/saved
        categoryIds.forEach { categoryId ->
            lifehackRepository.getLifehacksByCategory(categoryId, OrderBy.POPULAR, limit = 5)
                .getOrElse { emptyList() }
                .let { results.addAll(it) }
        }

        // 6. Lifehacks por historial de búsqueda
        searchHistory.take(3).forEach { query ->
            lifehackRepository.searchLifehacks(query, limit = 5)
                .getOrElse { emptyList() }
                .let { results.addAll(it) }
        }

        // 7. Elimina duplicados, quita los ya likeados/guardados, mezcla y limita
        val likedSavedIds = (likedLifehacks + savedLifehacks).map { it.id }.toSet()
        results
            .distinctBy { it.id }
            .filter { it.id !in likedSavedIds }
            .shuffled()
            .take(limit)
            .ifEmpty {
                // Si después de filtrar no queda nada, randoms
                lifehackRepository.getLifehacks(OrderBy.RECENT, limit)
                    .getOrElse { emptyList() }
                    .shuffled()
                    .take(limit)
            }
    }
}*/