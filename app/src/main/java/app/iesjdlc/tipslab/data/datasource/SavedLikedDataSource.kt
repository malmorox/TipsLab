package app.iesjdlc.tipslab.data.datasource

import kotlinx.coroutines.flow.Flow

interface SavedLikedDataSource {
    fun observeSavedIds(userId: String): Flow<List<String>>
    fun observeLikedIds(userId: String): Flow<List<String>>
    fun observeIsLiked(userId: String, lifehackId: String): Flow<Boolean>
    fun observeIsSaved(userId: String, lifehackId: String): Flow<Boolean>
    suspend fun toggleSaved(userId: String, lifehackId: String): Boolean
    suspend fun toggleLiked(userId: String, lifehackId: String): Boolean
    suspend fun getLikedCount(userId: String): Int
}