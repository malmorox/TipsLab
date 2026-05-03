package app.iesjdlc.tipslab.data.datasource.remote

import app.iesjdlc.tipslab.data.model.LifehackDto
import app.iesjdlc.tipslab.domain.repository.OrderBy

interface LifehackDataSource {
    suspend fun getByAuthor(authorId: String): List<LifehackDto>
    suspend fun getById(id: String): LifehackDto?
    suspend fun getByIds(ids: List<String>): List<LifehackDto>
    suspend fun getByCategory(
        categoryId: Int,
        orderBy: OrderBy,
        limit: Int
    ): List<LifehackDto>
    suspend fun searchByCategory(
        categoryId: Int,
        query: String,
        limit: Int = 20,
        offset: Int = 0
    ): List<LifehackDto>
    suspend fun create(dto: LifehackDto): String
    suspend fun update(id: String, dto: LifehackDto)
    suspend fun delete(id: String)
}