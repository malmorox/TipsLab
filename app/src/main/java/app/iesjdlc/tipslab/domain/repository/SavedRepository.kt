package app.iesjdlc.tipslab.domain.repository

import app.iesjdlc.tipslab.domain.model.Lifehack

interface SavedRepository {
    suspend fun getSavedLifehacks(): Result<List<Lifehack>>
    suspend fun isLifehackSaved(userId: String, lifehackId: String): Result<Boolean>
    suspend fun toggleSaved(lifehackId: String): Result<Boolean>
    suspend fun getSavedCount(): Result<Int>
}