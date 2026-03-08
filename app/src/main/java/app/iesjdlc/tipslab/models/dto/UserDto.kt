package app.iesjdlc.tipslab.models.dto

data class UserDto(
    val id: String = "",
    val username: String = "",
    val email: String = "",
    val avatar_url: String? = null,
)