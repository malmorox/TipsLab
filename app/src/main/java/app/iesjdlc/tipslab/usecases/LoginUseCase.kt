package app.iesjdlc.tipslab.usecases

import app.iesjdlc.tipslab.repository.AuthRepository
import app.iesjdlc.tipslab.repository.UserRepository

class LoginUseCase(
    private val authRepository: AuthRepository = AuthRepository(),
    private val userRepository: UserRepository = UserRepository()
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