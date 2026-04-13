package app.iesjdlc.tipslab.data.model

import com.google.firebase.firestore.PropertyName

data class UserDto(
    val id: String = "",
    val email: String = "",
    val username: String = "",

    @PropertyName("photo_url")
    val photoUrl: String = "",

    val provider: String = "BASIC",
)