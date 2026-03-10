package app.iesjdlc.tipslab.models.dto

data class UserDto(
    val id: String = "",
    val email: String = "",
    val username: String = "",
    val photo_url: String = "",
    val provider: String = "BASIC",
    val favorites: List<String> = emptyList()
)