package app.iesjdlc.tipslab.domain.model

data class User(
    val id: String,
    val email: String,
    val username: String,
    val photoUrl: String? = null,
    val provider: ProviderType = ProviderType.BASIC
)

enum class ProviderType {
    BASIC,
    GOOGLE
}