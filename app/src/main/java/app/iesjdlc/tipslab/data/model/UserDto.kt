package app.iesjdlc.tipslab.data.model

data class UserDto(
    val id: String = "",
    val email: String = "",
    val username: String = "",
    val photo_url: String = "",
    val provider: String = "BASIC",
)