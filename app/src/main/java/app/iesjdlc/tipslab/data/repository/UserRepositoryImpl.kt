package app.iesjdlc.tipslab.data.repository

import app.iesjdlc.tipslab.data.datasource.remote.UserDataSource
import app.iesjdlc.tipslab.data.mapper.UserMapper
import app.iesjdlc.tipslab.domain.model.User
import app.iesjdlc.tipslab.data.model.UserDto
import app.iesjdlc.tipslab.domain.repository.UserRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val dataSource: UserDataSource,
    private val mapper: UserMapper
) : UserRepository {
    override suspend fun existsUser(uid: String): Result<Boolean> = runCatching {
        dataSource.existsById(uid)
    }

    override suspend fun existsEmail(email: String): Result<Boolean> = runCatching {
        dataSource.existsByEmail(email)
    }

    override suspend fun existsUsername(username: String): Result<Boolean> = runCatching {
        dataSource.existsByUsername(username)
    }

    override suspend fun getEmailByUsername(username: String): Result<String?> = runCatching {
        dataSource.getEmailByUsername(username)
    }

    override suspend fun getUserById(id: String): Result<User> = runCatching {
        dataSource.getById(id)?.let { mapper.toDomain(it) } ?: error("Usuario no encontrado")
    }

    override suspend fun createUser(user: User): Result<Unit> = runCatching {
        dataSource.save(mapper.toDto(user))
    }

    override suspend fun createGoogleUser(user: User): Result<Unit> = runCatching {
        dataSource.save(mapper.toDto(user))
    }

    override suspend fun updateUser(user: User): Result<Unit> = runCatching {
        dataSource.update(user.id, mapper.toDto(user))
    }
}