package app.iesjdlc.tipslab.domain.repository

import app.iesjdlc.tipslab.data.repository.GoogleAuthResult
import app.iesjdlc.tipslab.domain.model.User
import com.google.firebase.auth.FirebaseUser

interface AuthRepository {
    suspend fun checkUserSession(): Boolean
    suspend fun loadProfile(uid: String)
    fun getCurrentUser(): User
    suspend fun login(email: String, password: String): Result<FirebaseUser>
    suspend fun signUp(email: String, password: String): Result<FirebaseUser>
    suspend fun authenticateWithGoogle(idToken: String): Result<GoogleAuthResult>
    fun logout()
}