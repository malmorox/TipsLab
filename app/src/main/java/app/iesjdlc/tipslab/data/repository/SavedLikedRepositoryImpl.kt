package app.iesjdlc.tipslab.data.repository

import app.iesjdlc.tipslab.data.datasource.LifehackDataSource
import app.iesjdlc.tipslab.data.datasource.SavedLikedDataSource
import app.iesjdlc.tipslab.data.resolver.LifehackResolver
import app.iesjdlc.tipslab.domain.model.Lifehack
import app.iesjdlc.tipslab.domain.repository.SavedLikedRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.transformLatest
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
class SavedLikedRepositoryImpl @Inject constructor(
    private val savedLikedDataSource: SavedLikedDataSource,
    private val lifehackDataSource: LifehackDataSource,
    private val resolver: LifehackResolver
) : SavedLikedRepository {
    override suspend fun getSavedIds(userId: String): List<String> =
        savedLikedDataSource.getSavedIds(userId)

    override suspend fun getLikedIds(userId: String): List<String> =
        savedLikedDataSource.getLikedIds(userId)

    override fun observeUserSavedLifehacks(uid: String): Flow<List<Lifehack>> =
        savedLikedDataSource.observeSavedIds(uid)
            .transformLatest { ids ->
                if (ids.isEmpty()) {
                    emit(emptyList())
                    return@transformLatest
                }
                emit(resolver.resolve(lifehackDataSource.getByIds(ids)))
            }

    override fun observeUserLikedLifehacks(uid: String): Flow<List<Lifehack>> =
        savedLikedDataSource.observeLikedIds(uid)
            .transformLatest { ids ->
                if (ids.isEmpty()) {
                    emit(emptyList())
                    return@transformLatest
                }
                emit(resolver.resolve(lifehackDataSource.getByIds(ids)))
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