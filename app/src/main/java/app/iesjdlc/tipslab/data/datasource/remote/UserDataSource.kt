package app.iesjdlc.tipslab.data.datasource.remote

import app.iesjdlc.tipslab.data.model.UserDto

interface UserDataSource {
    suspend fun existsById(uid: String): Boolean

    suspend fun existsByEmail(email: String): Boolean

    suspend fun existsByUsername(username: String): Boolean

    suspend fun getEmailByUsername(username: String): String?

    suspend fun getById(id: String): UserDto?

    suspend fun getByIds(ids: List<String>): Map<String, UserDto>

    suspend fun save(dto: UserDto)

    suspend fun update(id: String, dto: UserDto)
}