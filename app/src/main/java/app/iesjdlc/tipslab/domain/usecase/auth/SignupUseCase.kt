package app.iesjdlc.tipslab.domain.usecase.auth

import app.iesjdlc.tipslab.domain.model.User
import app.iesjdlc.tipslab.domain.repository.AuthRepository
import app.iesjdlc.tipslab.domain.repository.UserRepository
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(
        email: String,
        username: String,
        password: String
    ): Result<Unit> {
        if (userRepository.existsUsername(username).getOrThrow())
            return Result.failure(Exception("El nombre de usuario ya está en uso"))

        if (userRepository.existsEmail(email).getOrThrow())
            return Result.failure(Exception("El email ya está en uso"))

        // Crear usuario en Firebase Auth
        val authResult = authRepository.signUp(email, password)
            .getOrElse { return Result.failure(it) }

        // Guardar en Firestore
        val user = User(
            id = authResult.uid,
            email = email,
            username = username
        )
        userRepository.createUser(user)

        // Cargar perfil en memoria
        authRepository.loadProfile(authResult.uid)

        return Result.success(Unit)
    }
}