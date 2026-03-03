package app.iesjdlc.tipslab.models.domain

data class User(
    val id: String,
    val username: String,
    val email: String,
    val avatarUrl: String?,
)