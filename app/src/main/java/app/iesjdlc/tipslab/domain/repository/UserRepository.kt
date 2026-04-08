package app.iesjdlc.tipslab.domain.repository

import app.iesjdlc.tipslab.domain.model.User

interface UserRepository {
    suspend fun getUserById(userId: String): Result<User>
    suspend fun createUser(user: User) : Result<Unit>
}