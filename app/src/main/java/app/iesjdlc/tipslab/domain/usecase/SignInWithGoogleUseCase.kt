package app.iesjdlc.tipslab.domain.usecase

import app.iesjdlc.tipslab.domain.model.ProviderType
import app.iesjdlc.tipslab.domain.model.User
import app.iesjdlc.tipslab.data.repository.GoogleAuthResult
import app.iesjdlc.tipslab.data.repository.UserRepositoryImpl
import app.iesjdlc.tipslab.core.utils.StringUtils
import app.iesjdlc.tipslab.data.repository.AuthRepositoryImpl
import javax.inject.Inject

class SignInWithGoogleUseCase @Inject constructor(
    private val authRepository: AuthRepositoryImpl,
    private val userRepository: UserRepositoryImpl
) {
    suspend operator fun invoke(idToken: String): Result<Unit> {
        return try {
            when (val info = authRepository.authenticateWithGoogle(idToken).getOrThrow()) {
                is GoogleAuthResult.ExistingUser -> Unit // perfil ya cargado en el repositorio
                is GoogleAuthResult.NewUser -> {
                    val username = generateUniqueUsername(info.displayName, info.email)
                    val newUser = User(
                        id = info.uid,
                        email = info.email,
                        username = username,
                        photoUrl = info.photoUrl ?: "",
                        provider = ProviderType.GOOGLE
                    )
                    userRepository.createGoogleUser(newUser)
                }
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Genera un username único a partir del displayName o email de Google.
     * Si el candidato base ya existe en Firestore, añade un sufijo numérico
     * aleatorio hasta encontrar uno libre.
     */
    private suspend fun generateUniqueUsername(displayName: String?, email: String?): String {
        val base = StringUtils.buildUniqueGoogleUsername(displayName, email)

        if (!userRepository.existsUsername(base).getOrThrow()) return base

        repeat(10) {
            val candidate = "${base.take(15)}${(1000..9999).random()}"
            if (!userRepository.existsUsername(candidate).getOrThrow()) return candidate
        }

        // Fallback con timestamp para garantizar unicidad absoluta
        return "${base.take(13)}${System.currentTimeMillis() % 100000}"
    }
}

