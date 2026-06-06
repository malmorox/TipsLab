package app.iesjdlc.tipslab.domain.repository

import app.iesjdlc.tipslab.domain.model.Lifehack
import kotlinx.coroutines.flow.Flow

interface SavedLikedRepository {
    fun observeUserSavedLifehacks(uid: String): Flow<List<Lifehack>>
    fun observeUserLikedLifehacks(uid: String): Flow<List<Lifehack>>
    fun observeIsLiked(
        userId: String,
        lifehackId: String
    ): Flow<Boolean>
    fun observeIsSaved(
        userId: String,
        lifehackId: String
    ): Flow<Boolean>
    suspend fun toggleSaved(
        userId: String,
        lifehackId: String
    ): Result<Boolean>
    suspend fun toggleLiked(
        userId: String,
        lifehackId: String
    ): Result<Boolean>
    suspend fun getUserLikedCount(uid: String): Result<Int>
}