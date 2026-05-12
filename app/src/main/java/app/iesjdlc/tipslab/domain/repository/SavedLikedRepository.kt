package app.iesjdlc.tipslab.domain.repository

import app.iesjdlc.tipslab.domain.model.Lifehack

interface SavedLikedRepository {
    suspend fun getUserSavedLifehacks(uid: String): Result<List<Lifehack>>
    suspend fun getUserLikedLifehacks(uid: String): Result<List<Lifehack>>
    suspend fun isLifehackSaved(
        userId: String,
        lifehackId: String
    ): Result<Boolean>
    suspend fun isLifehackLiked(
        userId: String,
        lifehackId: String
    ): Result<Boolean>
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