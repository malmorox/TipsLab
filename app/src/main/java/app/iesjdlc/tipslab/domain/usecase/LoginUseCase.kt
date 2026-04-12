package app.iesjdlc.tipslab.domain.usecase

import app.iesjdlc.tipslab.domain.repository.AuthRepository
import app.iesjdlc.tipslab.domain.repository.UserRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(
        emailOrUsername: String,
        password: String
    ): Result<Unit> {
        // Resolvemos si es email o username
        val email = if (emailOrUsername.contains("@")) {
            emailOrUsername
        } else {
            userRepository.getEmailByUsername(emailOrUsername).getOrThrow()
                ?: return Result.failure(Exception("Nombre de usuario no encontrado"))
        }

        // Autenticamos
        val authResult = authRepository.login(email, password)
            .getOrElse { return Result.failure(it) }

        // El repositorio carga su propio estado
        authRepository.loadProfile(authResult.uid)

        return Result.success(Unit)
    }
}