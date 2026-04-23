package app.iesjdlc.tipslab.domain.repository

import app.iesjdlc.tipslab.domain.model.User

interface UserRepository {
    suspend fun existsUser(uid: String): Result<Boolean>
    suspend fun existsEmail(email: String): Result<Boolean>
    suspend fun existsUsername(username: String): Result<Boolean>
    suspend fun getEmailByUsername(username: String): Result<String?>
    suspend fun getUserById(id: String): Result<User>
    suspend fun createUser(user: User): Result<Unit>
    suspend fun createGoogleUser(user: User): Result<Unit>
    suspend fun updateUser(user: User): Result<Unit>
}