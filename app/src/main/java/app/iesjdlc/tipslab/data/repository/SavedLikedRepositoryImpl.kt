package app.iesjdlc.tipslab.data.repository

import app.iesjdlc.tipslab.data.datasource.LifehackDataSource
import app.iesjdlc.tipslab.data.datasource.SavedLikedDataSource
import app.iesjdlc.tipslab.data.resolver.LifehackResolver
import app.iesjdlc.tipslab.domain.model.Lifehack
import app.iesjdlc.tipslab.domain.repository.SavedLikedRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SavedLikedRepositoryImpl @Inject constructor(
    private val savedLikedDataSource: SavedLikedDataSource,
    private val lifehackDataSource: LifehackDataSource,
    private val resolver: LifehackResolver
) : SavedLikedRepository {
    override suspend fun getUserSavedLifehacks(uid: String): Result<List<Lifehack>> = runCatching {
        val savedIds = savedLikedDataSource.getSavedIds(uid)
        resolver.resolve(lifehackDataSource.getByIds(savedIds))
    }

    override suspend fun getUserLikedLifehacks(uid: String): Result<List<Lifehack>> = runCatching {
        val likedIds = savedLikedDataSource.getLikedIds(uid)
        resolver.resolve(lifehackDataSource.getByIds(likedIds))
    }

    override fun observeIsLiked(userId: String, lifehackId: String): Flow<Boolean> =
        savedLikedDataSource.observeIsLiked(userId, lifehackId)

    override fun observeIsSaved(userId: String, lifehackId: String): Flow<Boolean> =
        savedLikedDataSource.observeIsSaved(userId, lifehackId)

    override suspend fun toggleSaved(userId: String, lifehackId: String): Result<Boolean> = runCatching {
        savedLikedDataSource.toggleSaved(userId, lifehackId)
    }

    override suspend fun toggleLiked(userId: String, lifehackId: String): Result<Boolean> = runCatching {
        savedLikedDataSource.toggleLiked(userId, lifehackId)
    }

    override suspend fun getUserLikedCount(uid: String): Result<Int> = runCatching {
        savedLikedDataSource.getLikedCount(uid)
    }
}