package app.iesjdlc.tipslab.data.model

import com.google.firebase.firestore.PropertyName

data class UserDto(
    val id: String = "",
    val email: String = "",
    val username: String = "",

    @get:PropertyName("photo_url")
    @set:PropertyName("photo_url")
    var photoUrl: String? = null,

    val provider: String = "BASIC",
)