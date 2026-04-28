package app.iesjdlc.tipslab.data.datasource.remote

import app.iesjdlc.tipslab.data.model.LifehackDto

interface LifehackDataSource {
    suspend fun getByAuthor(authorId: String): List<LifehackDto>
    suspend fun getById(id: String): LifehackDto?
    suspend fun getByIds(ids: List<String>): List<LifehackDto>
    suspend fun getByCategory(categoryId: Int): List<LifehackDto>
    suspend fun create(dto: LifehackDto): String
    suspend fun update(id: String, dto: LifehackDto)
    suspend fun delete(id: String)
}