package app.iesjdlc.tipslab.data.datasource

import kotlinx.coroutines.flow.Flow

interface SavedLikedDataSource {
    suspend fun getSavedIds(userId: String): List<String>
    suspend fun getLikedIds(userId: String): List<String>
    fun observeIsLiked(userId: String, lifehackId: String): Flow<Boolean>
    fun observeIsSaved(userId: String, lifehackId: String): Flow<Boolean>
    suspend fun toggleSaved(userId: String, lifehackId: String): Boolean
    suspend fun toggleLiked(userId: String, lifehackId: String): Boolean
    suspend fun getLikedCount(userId: String): Int
}