package app.iesjdlc.tipslab.data.datasource.remote

import app.iesjdlc.tipslab.core.constants.DBConstants
import app.iesjdlc.tipslab.data.datasource.SearchDataSource
import app.iesjdlc.tipslab.data.model.LifehackDto
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class SearchRemoteDataSource @Inject constructor(
    private val db: FirebaseFirestore
) : SearchDataSource.Remote {
    override suspend fun getSearchSuggestions(
        query: String,
        limit: Int
    ): List<String> {
        val queryLower = query.lowercase().trim()

        return db.collection(DBConstants.Remote.LIFEHACKS_COLLECTION)
            .get().await()
            .documents
            .mapNotNull { it.getString(DBConstants.Remote.TITLE_FIELD) }
            .filter { it.lowercase().contains(queryLower) }
            .take(limit)
    }

    override suspend fun getSearchSuggestionsByCategory(
        categoryId: Int,
        query: String,
        limit: Int
    ): List<String> =
        db.collection(DBConstants.Remote.LIFEHACKS_COLLECTION)
            .whereEqualTo(DBConstants.Remote.CATEGORY_ID_FIELD, categoryId)
            .whereArrayContains("tags", query)
            .limit(limit.toLong())
            .get().await()
            .documents
            .mapNotNull { it.getString(DBConstants.Remote.TITLE_FIELD) }

    override suspend fun searchLifehacksByQuery(
        query: String,
        limit: Int,
        offset: Int
    ): List<LifehackDto> {
        val queryLower = query.lowercase().trim()

        return db.collection(DBConstants.Remote.LIFEHACKS_COLLECTION)
            .get().await()
            .documents
            .mapNotNull { it.toObject(LifehackDto::class.java) }
            .filter { dto ->
                dto.title.lowercase().contains(queryLower)
            }
    }

    override suspend fun searchLifehacksByCategory(
        categoryId: Int,
        query: String,
        limit: Int,
        offset: Int
    ): List<LifehackDto> =
        db.collection(DBConstants.Remote.LIFEHACKS_COLLECTION)
            .whereEqualTo(DBConstants.Remote.CATEGORY_ID_FIELD, categoryId)
            .whereArrayContains("tags", query)
            .get().await()
            .documents
            .mapNotNull { it.toObject(LifehackDto::class.java) }
}