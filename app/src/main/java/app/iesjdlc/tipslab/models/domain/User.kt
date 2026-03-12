package app.iesjdlc.tipslab.models.domain

data class User(
    val id: String = "",
    val email: String = "",
    val username: String = "",
    val photoUrl: String = "",
    val provider: ProviderType = ProviderType.BASIC
)

enum class ProviderType {
    BASIC,
    GOOGLE
}