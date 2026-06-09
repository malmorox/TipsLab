package app.iesjdlc.tipslab.domain.usecase.lifehack

import android.util.Log
import app.iesjdlc.tipslab.domain.model.Lifehack
import app.iesjdlc.tipslab.domain.repository.AuthRepository
import app.iesjdlc.tipslab.domain.repository.LifehackRepository
import app.iesjdlc.tipslab.domain.repository.SavedLikedRepository
import app.iesjdlc.tipslab.domain.repository.SearchRepository
import app.iesjdlc.tipslab.domain.repository.OrderBy
import javax.inject.Inject

class GetForYouLifehacksUseCase @Inject constructor(
    private val savedLikedRepository: SavedLikedRepository,
    private val lifehackRepository: LifehackRepository,
    private val searchRepository: SearchRepository,
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(limit: Int = 10): Result<List<Lifehack>> = runCatching {
        val user = authRepository.getCurrentUser()

        val likedIds = savedLikedRepository.getLikedIds(user.id)
        val savedIds = savedLikedRepository.getSavedIds(user.id)
        val interactedIds = (likedIds + savedIds).distinct().take(20)
        Log.d("ForYou", "likedIds: $likedIds")
        Log.d("ForYou", "savedIds: $savedIds")
        Log.d("ForYou", "interactedIds: $interactedIds")

        // Obtener las categorías de esos lifehacks
        val interactedLifehacks = if (interactedIds.isNotEmpty())
            lifehackRepository.getLifehacksByIds(interactedIds).getOrDefault(emptyList())
        else emptyList()
        Log.d("ForYou", "interactedLifehacks: ${interactedLifehacks.size}")
        // Contar cuántas veces aparece cada categoría (más interacciones = más peso)
        val categoryCounts = interactedLifehacks
            .groupBy { it.category.id }
            .mapValues { it.value.size }
            .toMutableMap()
        Log.d("ForYou", "categoryCounts: $categoryCounts")

        // Añadir peso por historial de búsqueda (menos peso que interacciones)
        val searchHistory = searchRepository.getSearchHistory(user.id).getOrDefault(emptyList())

        // Si no hay datos suficientes, devolver trending como fallback
        if (categoryCounts.isEmpty()) {
            return@runCatching lifehackRepository.getLifehacks(
                orderBy = OrderBy.POPULAR,
                limit = limit
            ).getOrDefault(emptyList())
        }

        // Obtener lifehacks de las categorías con más peso
        val topCategories = categoryCounts
            .entries
            .sortedByDescending { it.value }
            .take(3)
            .map { it.key }

        val results = mutableListOf<Lifehack>()
        val perCategory = (limit / topCategories.size).coerceAtLeast(1)

        for (categoryId in topCategories) {
            val lifehacks = lifehackRepository.getLifehacksByCategory(
                categoryId = categoryId,
                orderBy = OrderBy.POPULAR,
                limit = perCategory
            ).getOrDefault(emptyList())
            results.addAll(lifehacks)
        }

        // Excluir los que ya ha interactuado y mezclar un poco
        results
            .filter { it.id !in interactedIds && it.author.id != user.id }
            .shuffled()
            .take(limit)
    }
}