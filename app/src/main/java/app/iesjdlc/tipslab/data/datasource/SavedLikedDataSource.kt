package app.iesjdlc.tipslab.data.datasource

interface SavedLikedDataSource {
    suspend fun getSavedIds(userId: String): List<String>
    suspend fun getLikedIds(userId: String): List<String>
    suspend fun isLifehackSaved(userId: String, lifehackId: String): Boolean
    suspend fun isLifehackLiked(userId: String, lifehackId: String): Boolean
    suspend fun toggleSaved(userId: String, lifehackId: String): Boolean
    suspend fun toggleLiked(userId: String, lifehackId: String): Boolean
    suspend fun getLikedCount(userId: String): Int
}