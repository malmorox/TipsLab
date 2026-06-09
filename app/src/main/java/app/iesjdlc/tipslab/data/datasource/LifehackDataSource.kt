package app.iesjdlc.tipslab.data.datasource

import app.iesjdlc.tipslab.data.model.LifehackDto
import app.iesjdlc.tipslab.domain.repository.OrderBy
import kotlinx.coroutines.flow.Flow

interface LifehackDataSource {
    suspend fun getById(id: String): LifehackDto?
    suspend fun getByIds(ids: List<String>): List<LifehackDto>
    suspend fun get(
        orderBy: OrderBy,
        limit: Int
    ) : List<LifehackDto>
    suspend fun getByCategory(
        categoryId: Int,
        orderBy: OrderBy,
        limit: Int
    ): List<LifehackDto>
    fun observeById(id: String): Flow<LifehackDto>
    fun observeByAuthor(authorId: String): Flow<List<LifehackDto>>
    suspend fun create(dto: LifehackDto): String
    suspend fun update(id: String, dto: LifehackDto)
    suspend fun updateMedia(
        lifehackId: String,
        mediaUrl: String,
        mediaType: String
    )
    suspend fun delete(id: String)
}